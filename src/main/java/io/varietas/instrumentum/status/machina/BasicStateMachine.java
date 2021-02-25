/*
 * Copyright 2017 Michael Rhöse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.varietas.instrumentum.status.machina;

import io.varietas.instrumentum.status.machina.configurations.FSMConfiguration;
import io.varietas.instrumentum.status.machina.containers.ListenerContainer;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import io.varietas.instrumentum.status.machina.errors.InvalidTransitionException;
import io.varietas.instrumentum.status.machina.errors.InvalidTransitionListenerException;
import io.varietas.instrumentum.status.machina.errors.TransitionInvocationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>AbstractStateMachine</h2>
 * <p>
 * This class represents an abstract implementation of the {@link StateMachine} interface. The default implementation contains the firing of single transitions.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/7/2017
 */
@Slf4j
public abstract class BasicStateMachine implements StateMachine {

    protected static final String LISTENER_METHOD_BEFORE = "before";
    protected static final String LISTENER_METHOD_AFTER = "after";

    protected final FSMConfiguration configuration;

    public BasicStateMachine(final FSMConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * This method searches the container which contains all information required to performing the transition operation.
     *
     * @param event Next transition kind.
     * @param currentState Current state of target for identification if multiple transitions are available.
     *
     * @return Expected container for the transition, otherwise an empty Optional.
     */
    protected Optional<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> findTransitionContainer(final Enum<?> event, final Enum<?> currentState) {
        return this.configuration.getTransitions().stream()
                .filter(transit -> transit.getOn().equals(event) && (transit.getFrom().equals(currentState) || transit.getTo().equals(currentState)))
                .findFirst();
    }

    @Override
    public void fire(final Enum<?> transition, final Statable<?> target) throws TransitionInvocationException, InvalidTransitionException {

        final Optional<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> transitionContainer = this.findTransitionContainer(transition, target.state());

        if (!transitionContainer.isPresent()) {
            throw new InvalidTransitionException(transition, "State of target '" + target.state().name() + "' doesn't match required state for tarnsition '" + transition.name() + "'.");
        }

        this.fire(transitionContainer.get(), target);
    }

    /**
     * Performs the execution of a single transition. <b>Important:</b> The transition must be possible.
     *
     * @param transition Container of transition which has to be performed.
     * @param target Transition target.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void fire(final TransitionContainer<? extends Enum<?>, ? extends Enum<?>> transition, final Statable target) throws InvalidTransitionException {

        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("State change to {} entered.", transition.getOn());
            }

            if (Objects.nonNull(transition.getListeners())) {
                transition.getListeners().forEach(listener -> this.executeListener(listener, LISTENER_METHOD_BEFORE, transition.getOn(), target));
            }

            transition.getCalledMethod().invoke(this, transition.getFrom(), transition.getTo(), transition.getOn(), target);
            target.state(transition.getTo());

            if (Objects.nonNull(transition.getListeners())) {
                transition.getListeners().forEach(listener -> this.executeListener(listener, LISTENER_METHOD_AFTER, transition.getOn(), target));
            }

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("State change to {} finished.", transition.getOn());
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new TransitionInvocationException(transition.getOn(), transition.getCalledMethod().getName(), ex.getLocalizedMessage());
        }
    }

    protected void executeListener(final ListenerContainer listener, final String methodName, final Enum<?> on, final Object target) {

        if (methodName.equals(LISTENER_METHOD_BEFORE) && !listener.isBefore()) {
            return;
        }

        if (methodName.equals(LISTENER_METHOD_AFTER) && !listener.isAfter()) {
            return;
        }

        try {
            Object listenerInstance = listener.getListener().getDeclaredConstructor().newInstance();
            Method method = listener.getListener().getMethod(methodName, on.getDeclaringClass(), target.getClass());
            method.invoke(listenerInstance, on, target);
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Couldn't call listener method '{}'. {}: {}", methodName, ex.getClass().getSimpleName(), ((Objects.nonNull(ex.getMessage())) ? ex.getMessage() : "No message available"));
            }
            throw new InvalidTransitionListenerException(listener.getListener(), "Listener on method '" + methodName + ".", ex);
        }
    }
}
