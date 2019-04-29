# Welcome to TraceOrdering Project  
> Ordering and Replaying of Execution Traces of Distributed Systems in the Context of Model-driven Development

Ordering and replaying of execution traces of distributed systems is a challenging problem. State-of-the-art approaches annotate the traces with logical/physical timestamps and then order them in accordance with their timestamps. Physical timestamps are often inaccurate due to issues of synchronization and clock precision, and logical timestamps cause an increase in the size of traces as the number of nodes in the distributed system increases. This paper concerns trace ordering and replay in the context of model-driven development of distributed systems. By leveraging the abstract nature of models and existing model analysis and transformation techniques, we introduce a fully automated, efficient, and accurate method of trace collection and replay, without the use of timestamps. Instead, we use an abstract interpretation of models which
orders and replays traces using the causality relation between traces.

## A demonstration Video
> In order to show how our approach really works, we use and opensource web-based diagraming tool called @drawio which uses  mxGraph library. You can find more information in: [Drawio in Github](https://github.com/jgraph/drawio).


[<p style="text-align:center;"><img src="https://i.ibb.co/nbM8rL6/You-Tube-icon.png" width="193" height="130"></p>](https://youtu.be/SLnO32cWDI0)


## Source code layout
    .
    ├── com.controller                # All files for Creating Abstract Interpreter and Synthesizing variable values
    ├── com.antler4AC                 # All files for performing Action Code analysis  
    ├── com.server                    # All files for making the TCP socket connection between distributed clients and server 
    ├── com.umlrtParser               # All files for performing structural/behavioural static analysis and creating PES
    ├── JAR                           # All required JAR files that should be added to the project 
    ├── Experiments                   
    │   ├── Original                  # Original Models (including: Replication.zip, ParcelRouter.zip , ...)
    │   ├── PhysicalTimeStamp         # Models that annotate traces with timestamps (e.g., MDebugger)
    |   ├── VectorTime                # Models that annotate traces with Vector-Time
    │   └── TimeStampFree             # Models that use no timestamp
    └── MDebugger                     
    │   ├── DebuggerModel             # The Debugging Agent which is developed using UML-RT  
    |   ├── Model_instrumentation     # All the developed script for the model transformation 
    |   ├── RealTimeLibs              # All lib that should be added into the RTS directory
    │   ├── MetaModels                # All required metamodels for executing the transformation
    |   │   ├── Ecore.ecore
    |   │   ├── RTCppProperties.ecore
    |   │   ├── RTCppProperties.profile.uml
    |   |   ├── UML.ecore
    |   │   ├── UMLPrimitiveType.uml
    |   │   ├── UMLRT-RTS.uml
    |   │   ├── UMLRTStateMachines.ecore
    |   │   ├── UMLRealTime.ecore
    |   │   ├── UMLRealTimeSM-addendum.dsmlvalidation.uml
    |   │   ├── UMLRealTimeSM-addendum.profile.uml
    |   │   ├── UMLRealTimeSM.genmodel
    |   │   ├── default.ecore
    |   │   ├── uml-rt.dsmlvalidation.uml
    |   │   ├── uml-rt.genmode
    |   │   └── uml-rt.profile.uml

#### Main loop of the transformation
```
1 addGateWay () ;
2 refineStructure () ;
3 for ( SM in allStateMachines ){
4   refineForSRO ( SM );
5   for (s in allStates ) {
6     s.addTrace ( traceType );
7     s.guardCodes () ;
8   }
9  }
```

The above code shows the main function for instrumenting the state machine of all capsules of the user-defined model. The
addGateway function is responsible for enabling the model to interface with the debugger. It adds a UML-RT port to
each capsule.


## Background

The following links may provide useful resources regarding the UML-RT concepts, and using PapyrusRT.

[PapyrusRT Website](https://eclipse.org/papyrus-rt/)

[PapyrusRT Forums](https://www.eclipse.org/forums/index.php/f/314/)

[Getting Started with PapyrusRT](https://wiki.eclipse.org/Papyrus-RT/User/User_Guide/Getting_Started)

[UML-RT Language Concepts](https://pdfs.semanticscholar.org/7fae/fac63155a404e431c97201f89fc8c37a7d62.pdf)

[An executable formal semantics for UML-RT](https://link.springer.com/article/10.1007/s10270-014-0399-z)
