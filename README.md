# Welcome to TraceOrdering Project  
> Ordering and Replaying of Execution Traces of Distributed Systems in the Context of Model-driven Development

Ordering and replaying of execution traces of distributed systems is a challenging problem. State-of-the-art approaches annotate the traces with logical/physical timestamps and then order them in accordance with their timestamps. Physical timestamps are often inaccurate due to issues of synchronization and clock precision, and logical timestamps cause an increase in the size of traces as the number of nodes in the distributed system increases. This paper concerns trace ordering and replay in the context of model-driven development of distributed systems. By leveraging the abstract nature of models and existing model analysis and transformation techniques, we introduce a fully automated, efficient, and accurate method of trace collection and replay, without the use of timestamps. Instead, we use an abstract interpretation of models which
orders and replays traces using the causality relation between traces.

## A Demonstration Video
> The graphical user interface of our tool is developed on top of the   @drawio which uses mxGraph library. You can find more information in [Drawio in Github](https://github.com/jgraph/drawio).


[<p style="text-align:center;"><img src="https://i.ibb.co/nbM8rL6/You-Tube-icon.png" width="193" height="130"></p>](https://youtu.be/KKO3KV9SqBI)


## Source code layout
    .
    ├──src
    |   ├── com.controller                # All files for Creating Abstract Interpreter and Synthesizing variable values
    |   ├── com.antler4AC                 # All files for performing Action Code analysis  
    |   ├── com.server                    # All files for receiving traces from distributed clients
    |   ├── com.umlrtParser               # All files for performing structural/behavioral static analysis and creating PES
    ├── JAR                               # All required JAR files that should be added to the project 
    ├── Experiments                   
    │   ├── Original                      # Original Models (including: Replication.zip, ParcelRouter.zip , ...)
    │   ├── PhysicalTimeStamp             # Models that annotate traces with timestamps (e.g., MDebugger)
    |   ├── VectorTime                    # Models that annotate traces with Vector-Time
    │   └── TimeStampFree                 # Models that use no timestamp
    └── MDebugger                     
    │   ├── DebuggerModel                 # The Debugging Agent which is developed using UML-RT  
    |   ├── Model_instrumentation         # All the developed script for the model transformation 
    |   ├── RealTimeLibs                  # All lib that should be added into the RTS directory
    │   └── MetaModels                    # All required metamodels for executing the transformation


# Usage
First, import the JAR files into the project/libraries. Then add the UML file of the original models in the Experiments into the project/resources. Finally, run the controller at (src/com/controller/Controller.java). It takes a couple of seconds to perform static analysis and extract all the Possible Execution Paths (PES) form the UML file.

Now the software is ready to receive traces from clients at TCP port 8001.


In order to provide distributed clients from the given models, just follow the instruction at [Distribution for UML-RT](https://github.com/kjahed/papyrusrt-distribution).

## Background

The following links may provide useful resources regarding the UML-RT concepts, and using PapyrusRT.

[PapyrusRT Website](https://eclipse.org/papyrus-rt/)

[PapyrusRT Forums](https://www.eclipse.org/forums/index.php/f/314/)

[Getting Started with PapyrusRT](https://wiki.eclipse.org/Papyrus-RT/User/User_Guide/Getting_Started)

[UML-RT Language Concepts](https://pdfs.semanticscholar.org/7fae/fac63155a404e431c97201f89fc8c37a7d62.pdf)

[An executable formal semantics for UML-RT](https://link.springer.com/article/10.1007/s10270-014-0399-z)

[Distribution for UML-RT](https://github.com/kjahed/papyrusrt-distribution)
