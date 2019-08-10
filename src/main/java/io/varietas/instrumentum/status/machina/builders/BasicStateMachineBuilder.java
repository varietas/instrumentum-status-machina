/*
 * Copyright 2018 Michael Rhöse.
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
package io.varietas.instrumentum.status.machina.builders;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.annotations.Transition;
import io.varietas.instrumentum.status.machina.annotations.TransitionListener;
import io.varietas.instrumentum.status.machina.annotations.TransitionListeners;
import io.varietas.instrumentum.status.machina.annotations.Transitions;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.containers.ListenerContainer;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import io.varietas.instrumentum.status.machina.error.MachineCreationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>BasicStateMachineBuilder</h2>
 * <p>
 * This class represents the common of all FSM builders. Please see specific implementations for usage.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 06/17/2018
 * @param <CONFIGURATION> Generic type of configuration that is created by the builder.
 */
@Slf4j
abstract class BasicStateMachineBuilder<CONFIGURATION extends FSMConfiguration> implements StateMachineBuilder<CONFIGURATION> {

    @Getter
    @Accessors(fluent = true, chain = true)
    @Setter
    protected CONFIGURATION configuration;
    protected Class<? extends Enum<?>> stateType;
    protected Class<? extends Enum<?>> eventType;

    protected final List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> transitions = new ArrayList<>();

    /**
     * Builds an instance of the {@link StateMachine}. The instance is initialised with the configuration of the type.
     *
     * @return Instance of the state machine.
     *
     * @throws MachineCreationException Thrown if an error occurred. Possible errors are typically reflection exceptions e.g. InstantiationException, IllegalAccessException and so on.
     */
    @Override
    public StateMachine build() throws MachineCreationException {

        try {
            return this.configuration.getMachineType().getConstructor(FSMConfiguration.class).newInstance(this.configuration);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            throw new MachineCreationException(this.configuration.getMachineType(), ex.getMessage());
        }
    }

    /**
     * Collects available transitions from the {@link StateMachine}. Transitions are identified by the {@link Transition} annotation.
     *
     * @param machineType The machine where the transitions are configured.
     *
     * @return List of all available transitions.
     */
    protected List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> collectTransitions(final Class<? extends StateMachine> machineType) {
        return Stream.of(machineType.getMethods())
                .filter(method -> method.isAnnotationPresent(Transitions.class) || method.isAnnotationPresent(Transition.class))
                .map(this::createTransitionContainers)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Creates transition containers available on a single method. One method can used by multiple transitions. Each transition is configured by a single {@link Transition} annotation. So, multiple {@link Transition} annotations can be available on a single method.
     *
     * @param method Method where the transitions are configured.
     *
     * @return List of all available transitions as transition containers.
     */
    private List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> createTransitionContainers(final Method method) {

        final List<ListenerContainer> listeners = this.extractTransitionListener(method);

        return Arrays.asList(method.getAnnotationsByType(Transition.class)).stream()
                .map((Transition transition) -> this.createTransitionContainer(transition, method, listeners))
                .collect(Collectors.toList());
    }

    private TransitionContainer<? extends Enum<?>, ? extends Enum<?>> createTransitionContainer(final Transition transition, final Method method, final List<ListenerContainer> listeners) {
        @SuppressWarnings("rawtypes")
        final Class<? extends Enum> stateClazzType = this.stateType;
        @SuppressWarnings("rawtypes")
        final Class<? extends Enum> eventClazzType = this.eventType;

        @SuppressWarnings("unchecked")
        final Enum<?> from = Enum.valueOf(stateClazzType, transition.from());
        @SuppressWarnings("unchecked")
        final Enum<?> to = Enum.valueOf(stateClazzType, transition.to());
        @SuppressWarnings("unchecked")
        final Enum<?> on = Enum.valueOf(eventClazzType, transition.on());

        if (LOGGER.isErrorEnabled()) {
            LOGGER.debug("Transition from '{}' to '{}' on '{}' will be created.", from.name(), to.name(), on.name());
            LOGGER.debug("{} listeners for transition {} added.", (Objects.nonNull(listeners) ? listeners.size() : 0), on.name());
        }
        return TransitionContainer.of(from, to, on, method).andAddListeners(listeners);
    }

    private List<ListenerContainer> extractTransitionListener(final Method method) {
        if (!method.isAnnotationPresent(TransitionListeners.class) && !method.isAnnotationPresent(TransitionListener.class)) {
            return null;
        }

        return Stream.of(method.getAnnotationsByType(TransitionListener.class))
                .map(annot -> {
                    final Class<?> listener = annot.value();
                    return ListenerContainer.of(listener, this.existsMethod(listener, "before"), this.existsMethod(listener, "after"));
                })
                .collect(Collectors.toList());
    }

    /**
     * Checks for the existing of a method on a given type.
     *
     * @param type       Type which has to be checked for the existing of a method.
     * @param methodName Method that is searched on a type.
     *
     * @return True if the method exists on the type, otherwise false.
     */
    protected final boolean existsMethod(final Class<?> type, final String methodName) {
        return Stream.of(type.getMethods()).anyMatch(method -> method.getName().equals(methodName));
    }
}
