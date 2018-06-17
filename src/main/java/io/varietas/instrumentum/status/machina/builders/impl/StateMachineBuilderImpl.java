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
package io.varietas.instrumentum.status.machina.builders.impl;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.configuration.impl.FSMConfigurationImpl;
import io.varietas.instrumentum.status.machina.annotations.StateMachineConfiguration;
import io.varietas.instrumentum.status.machina.annotations.Transition;
import io.varietas.instrumentum.status.machina.annotations.TransitionListener;
import io.varietas.instrumentum.status.machina.annotations.TransitionListeners;
import io.varietas.instrumentum.status.machina.annotations.Transitions;
import io.varietas.instrumentum.status.machina.builders.StateMachineBuilder;
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
 * <h2>StateMachineBuilderImpl</h2>
 * <p>
 * Implementation of the {@link StateMachineBuilder} interface. This builder is for simple state machines.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/9/2017
 */
@Slf4j
public class StateMachineBuilderImpl implements StateMachineBuilder {

    protected Class<? extends StateMachine> machineType;
    @Getter
    @Accessors(fluent = true, chain = true)
    @Setter
    protected FSMConfiguration configuration;

    protected Class<? extends Enum> stateType;
    protected Class<? extends Enum> eventType;

    protected final List<TransitionContainer> transitions = new ArrayList<>();

    /**
     * Extracts the configuration from a given {@link StateMachine}. This process should be done only once per state machine type and shared between the instances because the collection of information
     * is a big process and can take a while.
     *
     * @param machineType State machine type where the configuration is present.
     *
     * @return The instance of the builder for a fluent like API.
     */
    @Override
    public StateMachineBuilderImpl extractConfiguration(final Class<? extends StateMachine> machineType) {
        this.machineType = machineType;

        StateMachineConfiguration machineConfiguration = this.machineType.getAnnotation(StateMachineConfiguration.class);

        this.stateType = machineConfiguration.stateType();
        this.eventType = machineConfiguration.eventType();

        this.transitions.addAll(this.collectTransitions());

        this.configuration = new FSMConfigurationImpl(
                this.transitions,
                this.stateType,
                this.eventType);

        LOGGER.debug("Configuration for '{}' created:\n"
                + "-> {} transitions collected\n"
                + "-> {} used for state type\n"
                + "-> {} used for event type.",
                this.machineType.getSimpleName(),
                this.transitions.size(),
                this.stateType.getSimpleName(),
                this.eventType.getSimpleName()
        );

        return this;
    }

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
            return this.machineType.getConstructor(FSMConfiguration.class).newInstance(this.configuration);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            throw new MachineCreationException(this.machineType, ex.getMessage());
        }
    }

    /**
     * Collects available transitions from the {@link StateMachine}. Transitions are identified by the {@link Transition} annotation.
     *
     * @return List of all available transitions.
     */
    protected List<TransitionContainer> collectTransitions() {
        return Stream.of(this.machineType.getMethods())
                .filter(method -> method.isAnnotationPresent(Transitions.class) || method.isAnnotationPresent(Transition.class))
                .map(this::createTransitionContainers)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Creates transition containers available on a single method. One method can used by multiple transitions. Each transition is configured by a single {@link Transition} annotation. So, multiple
     * {@link Transition} annotations can be available on a single method.
     *
     * @param method Method where the transitions are configured.
     *
     * @return List of all available transitions as transition containers.
     */
    private List<TransitionContainer> createTransitionContainers(final Method method) {

        final List<ListenerContainer> listeners = this.extractTransitionListener(method);

        return Arrays.asList(method.getAnnotationsByType(Transition.class)).stream()
                .map((Transition transition) -> this.createTransitionContainer(transition, method, listeners))
                .collect(Collectors.toList());
    }

    private TransitionContainer createTransitionContainer(final Transition transition, final Method method, final List<ListenerContainer> listeners) {
        Enum from = Enum.valueOf(this.stateType, transition.from());
        Enum to = Enum.valueOf(stateType, transition.to());
        Enum on = Enum.valueOf(this.eventType, transition.on());
        LOGGER.debug("Transition from '{}' to '{}' on '{}' will be created.", from.name(), to.name(), on.name());
        LOGGER.debug("{} listeners for transition {} added.", (Objects.nonNull(listeners) ? listeners.size() : 0), on.name());
        return new TransitionContainer<>(
                from,
                to,
                on,
                method,
                listeners
        );
    }

    private List<ListenerContainer> extractTransitionListener(final Method method) {
        if (!method.isAnnotationPresent(TransitionListeners.class) && !method.isAnnotationPresent(TransitionListener.class)) {
            return null;
        }

        return Stream.of(method.getAnnotationsByType(TransitionListener.class))
                .map(annot -> {
                    Class<?> listener = annot.value();
                    return new ListenerContainer(listener, this.existsMethod(listener, "before"), this.existsMethod(listener, "after"));
                })
                .collect(Collectors.toList());
    }

    protected final boolean existsMethod(final Class<?> type, final String methodName) {
        return Stream.of(type.getMethods()).filter(method -> method.getName().equals(methodName)).findFirst().isPresent();
    }
}
