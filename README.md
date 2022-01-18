[![OSI](https://res-5.cloudinary.com/crunchbase-production/image/upload/c_lpad,h_15,w_15,f_auto,q_auto:eco/v1413265600/yos3vcohir2yxnb3jtpf.png)](https://opensource.org) **We love Open Source**

# Finite state machine by varietas.io

The finite state machine library contains all necessary functionality for usage of a finite state machine. This is a basic implementation and does not support fancy functionality like e.g. Spring FSM.

### Usage

The current version is not deployed to maven. For now, you need to build it locally.

### FSM

The usage of an FSM is really easy. Create a class that extends `BasicStateMachine` and add a default constructor. Now, add all methods that represents the state transition functions and add the required annotations.

#### Configuration and implementation

```java
@StateMachineConfiguration(stateType = ExampleState.class, eventType = ExampleEvent.class)
public class ExampleStateMachine extends BasicStateMachine {

    private ExampleStateMachine(FSMConfiguration configuration) {
        super(configuration);
    }

    @Transition(from = ExampleState.DEACTIVATED.name(), on = ExampleState.PARK.name(), to = ExampleEvent.PARKED.name())
    public void fromDeactivatedToParked(final ExampleState from, final ExampleState to, final ExampleEvent event, final TestEntity entity) {
        entity.setValue(entity.getValue() - 5);
    }
}
```

_status maschina_ manages the states and events via enumerations. The `@StateMachineConfiguration` annotation contains that basic configuration for the FSM. 
That enumerations must be defined by the project. The `stateType` is the enumeration that defines constants for the different states in which an entity can be. The `eventType` is an enumeration that defines constants that describe the state transition functions.

Each state transition function must be a public function with a `@Transition` annotation on it. It is possible to use a function for multiple transitions. Simply add multiple `@Transition` annotations. *status maschina* will look for the required transition on all class methods where the `from` state matches the current entity state.

The target entity must implement the `Statable` interface. This interface exposes the methods state of the entity.

```java
public class TestEntity implements Statable<ExampleState> {

    private ExampleState state;

    // ...

    @Override
    public ExampleState state() {
        return this.state;
    }

    @Override
    public void state(ExampleState state) {
        this.state = state;
    }
}
```



#### How to get an instance

In your code, you need to create an instance of the FSM via the `StateMachineFactory`.

```java
StateMachine stateMachine = StateMachineFactory.getStateMachine(ExampleStateMachine.class)
```

It is also possible to use a builder for creating the configuration and creating the FSM instance via this configuration. The reuse of the configuration speeds up the FSM creation and is suggested in frameworks like Spring or if you know that the FSM will be used very often.

```java
final FSMConfiguration config = SimpleStateMachineBuilder.getBuilder()
    .extractConfiguration(ExampleStateMachine.class)
    ..configuration();
```

Now, you can create multiple instances via this configuration.

```java
final StateMachine stateMachine = SimpleStateMachineBuilder.getBuilder()
    .configuration(config)
    .build();
```

#### How to fire a state change

After creating a FSM instance, the maschine is ready to fire a state change event. 