# Hub App
## What does Hub the accomplish? 
Facilitates multiple servers, which addresses scalability, i.e. when a program becomes to large. 
Multiple servers:
    1) Guarantee high performance 
    2) Higher Security
    3) Efficient memory allocation

## How does the Hub work?
The Hub is a server that acts a map and guide for all of the other servers. More specifically, the hub stores and sends a map of all the server urls to all servers. 

## How to Run the Program
To emulate running multiple servers by running multiple quarkus applictions in different terminals. We set each application's Port number to be different so that the quarkus applications will not conflict. 

## Our Code
### Webclient
Webclient is an API that connect our different servers. More specifically, it provides methods for sending and receiving data from a data source identified by a URI. 

## SmallRye Health - Health Checks
SmallRye Health allows applications to provide information about their state to external viewers which is typically useful in cloud environments where automated processes must be able to determine whether the application should be discarded or restarted.

To Do List
    - Install extensions from quarkus to allow checkouts to exist and allow updates to happen periodically
        - 
    - Program updating all servers periodically
        - 
    - Program periodic check
        - 
    - Produce some result for a sub-par health check
        - 
    - Make a video