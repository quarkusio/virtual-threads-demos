# virtual-threads-demos

A set of Quarkus applications utilizing virtual threads


**IMPORTANT:** You need Java 21 to build and run the demos. 

## Build the demos

Each demo contains a `justfile` providing the main _recipes_ to build and run the demos.
We recommend [just]https://github.com/casey/just to run the demo. 
Alternatively, you can run the command manually.

```shell
> just build # Build the application
> just dev # Run the application in dev mode
> just run # Build an run the application
```