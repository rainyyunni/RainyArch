# RainyArch
## What's RainyArch?
RainyArch is a set of frameworks and application prototypes for Agile Development with Java and .Net and JavaScript.

You can use the application prototypes (on top of the frameworks) to get a quick start of your application development.
Just download (the source), set up, build and run, then it is yours.
With the prototype running on your server, you've finished the first round iteration of your application's agile development.
(See www.51chunzhen.com for detailed introduction and the prototype on the run.)

The frameworks provide simple and robust programming interfaces and patterns so that the prototypes can smoothly evolve and scale up to a full-featured application to meet your customers' requirements.
The prototypes are built on top of the frameworks. But if you're not interested in the prototypes, you can use the frameworks alone and develop your own applications on top.

## What are in RainyArch?
RainyArch contains two application prototypes and three frameworks.

1. java web project JavaNgArch  - a web application with a Mysql database.
2. C# solution NetNgArch  - a web application with a MSSqlServer database.
3. java package named "projectbase" in JavaNgArch - a server-side framework encapsulating and integrating Hibernate+SpringIOC+SpringMVC+JSP.
4. C# project named "ProjectBase" in NetNgArch - a server-side framework encapsulating and integrating NHibernate+CastleWindsorIOC+ASP.NetMVC+Spark/Razor.
5. javascript files named "ProjectBase_*.js" - a client-side framework encapsulating and integrating AngularJS+AngularUI(bootstrap)+UIRouter+AutoValidate.

You can combine one of the server-side frameworks with the client-side framework as the prototypes do. They are bounded to cooperate so that programming for both sides can easily and smoothly proceed as a whole. 
 
Or you can just choose to use any one of the frameworks alone. For instance, if your application has a non-browser client, or a server which is not written in java or C#, you are free to use whatever you have or you'll create for one side, as long as you make them match the simple interfaces the framework provides for the other side. 
In fact, the prototypes even contain a desktop client sample to show how the server-side framework works without the client-side framework.

## Why RainyArch?
1. **Agile development**. The prototypes get the first round iteration of your application development done within a day. 
Customers and engineers will directly see the system running and they can discuss requirements and design by actually using the evolving application from the very beginning and in every subsequent iteration. 
They can really work together based on a same thing - the target software, instead of using some mockup like static pages for customers and a pile of incomplete code files for engineers. And that is right one of the principles of Agile Development.

More for Agile Development, RainyArch also comes along with automation tools to generate database objects and CURD code files from UI to backend DAO all from a same design document. 
Add those auto-generated stuffs to the project and build, all primitive functions will be running on a clicking. It's really agile, isn't it?

2. **Double platforms**. Coding experiences will be the same for Java and .Net platform. The two server-side frameworks employ identical architectures and design patterns, and provide same programming interfaces and coding patterns to the application level. Application code on one platform has its counterpart on another platform even on line-to-line basis. With that and the same client-side framework, developers can easily do projects on both platforms.

3. **Framework on frameworks**. As top-level frameworks, RainyArch encapsulates and integrates popular general frameworks such as Hibernate, IOC, MVC, Angular, and employs design patterns and concepts and best practices, to make the programming interfaces simpler but more powerful.  

4. **Double sides binding**. The server-side and client-side frameworks can be seamlessly integrated as a whole where bidirectional data-binding between both sides works automatically, and even same with the binding between the two MVC structures on both sides. Taking the "auto-Ajax" feature for instance, without a single line of JavaScript, you can get a button to submit an Ajax request on clicking and deal with the response from the server automatically.  

5. **Simpler is better**. The frameworks simplify programming interfaces and standardize coding patterns. Developers are saved from making technical choices and solving technical problems so that they can focus on implementing business logic. Those frameworks constrain and standardize developers' work and cut down chances they could make mistakes, based on code instead of documents. Thus, quality of applications is ensured by the frameworks and not dependent on individual developers. On the other hand, the frameworks are compatible and supportive with advanced programming directly against any lower-level frameworks.   

6. **Less is more**. Solutions for well-known application-wide problems are implemented within the frameworks. They are auto-mapping, auto-IOC, auto-dictionary, authentication and authorization, table sorting and paging, auto-catching of db exceptions, languages switching, breadcrumb navigation, etc. In most cases, not a single line of client code is needed to turn those on. The frameworks are meant to reduce developers' code as much as possible.

7. **Taking advantage of new language features**. RainyArch employs latest languages (C#4.0/java8) features such as generic, linq, lambda expression, anonymous class, annotation, properties declaration, extension functions, etc. Code is cleaner and more elegant and easier to write and maintain with them. Developers can follow to upgrade their language skill at a fast pace.

8. **Final is stable**. RainyArch frameworks are the outermost frameworks that encapsulate other frameworks and programming techniques, so that they can isolate updates/upgrades to other frameworks and embrace upcoming new techniques but still provide stable, compatible and consistent interfaces to application code. They minimize cost of learning and changing for developers.

9. **Mature and improving**. The author has been doing programming and architecting work for more than 20 years, and has built frameworks for multi programming languages and platforms, which have all been employed in enterprise projects and got verified. With RainyArch being open source, hope it to be improved and augmented by a community and make developer enjoy programming and applications development get done with high quality.    
 
## How to use RainyArch?
See https://rainyyunni.github.io/RainyArch/ for documentation.
