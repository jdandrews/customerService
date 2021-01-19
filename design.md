# Get Started
While I haven't tested this on a cold system, I think it will work out of the box if you do the following:

1. Install Java 11; I'm using java 11.0.3 from OpenJDK
2. Install a relatively recent version of Gradle
3. ```./gradlew quarkusDev``` should build and launch the service locally in "development mode", which is nice because it rebuilds and redeploys any Java changes you make immediately.
4. I am testing with Postman. There is one working integration test (CustomerResourceTest.java). The API is described below in the "API" section. 

Basic auth is implemented. See ./src/main/resources/application.properties for login IDs.

Some things to try:

* Retrieve a customer: Login as Alice; hit http://localhost:8080/customer/1
   * you can use and ID value between 1 and 10; other values will return an error
   * Try logging in as different users to see different behavior. If you're using Chrome, you'll need to clear cookies between attempts, as it caches the login.
* Store a filter, then retrieve it (use the Alice or Bob logins):
   * post to ```http://localhost:8080/customers```: {"idList" : [ 2, 7 ]}. Note the plural in the URL. Note the return ID (probably 0).
   * get ```http://localhost:8080/customers/0``` (substitute the noted id, probably 0)
   * post to ```http://localhost:8080/servicecases```: {"customerId":7}; note the return ID
   * get ```http://localhost:8080/servicecases/0```
	* store a couple of filters and re-use them.

# Requirements
## Setting
We are building a new Customer Relationship Management (CRM) System with a layer of Artificial Intelligence powered pluggable components. 

As DMD’s Backend Engineer, your first major task will be the development of completely new software modules and packaged libraries using Python or JVM based language to power our customer facing banking solution. 

To simulate a real world workflow, we ask you to create a microservice with RESTFul API to expose customers data - profile information, contact information, service case history, and some confidential information like SSN.  Use a small generated test dataset (10 customers) to implement this behavior. It is not required to support a real backend database for this project so all your data can reside in file or in-memory. 

## Your task
What is a rough sketch for the architecture of this backend service?

A working code in a zip archive and instructions to run this on a Macbook. 

List all assumptions made while building this backend service and any known issues

### Scoping questions and definitions
*Service case history* is history of cases raised by customers. I will assume:

* There is some separate service providing case management; that service will provide an API that allows service history retrieval by customer ID, and 
* the resulting records can be expressed any way I want them, since that service isn't deployed yet

**What storage/retrieval keys are expected** (this helps define the API)**?**

> Please provide your suggestion.

**What is the expected load profile?**

Production save/retrieve/update/delete rates, latency/throughput requirements drives both API design and back-end architecture.

> Make fair assumptions for a CRM system.

**How many security profiles are expected?**
At least 3 come to mind:
* Those who can see service history retrieved by, say, a customer ID
* Those who can see personally-identifiable information (PII)
* Those who can see confidential information (e.g. SSN)

> Please provide your suggestion.

**How dynamic is the client list expected to be?**
This usually means “internal” vs. “external” customers—the latter is typically much more dynamic than the former, and so typically requires some kind of API for management.

> Make fair assumptions for a CRM system.

**What data representations is LivePerson already familiar with/using?**
E.g. JSON, XML, binary-on-the-wire, others?

**Is LivePerson already committed to any particular existing AuthZ/AuthN framework?**
E.g. certs at both ends of the connection, tokens (e.g. OAuth on an existing service, others?)

**Does LivePerson use existing infrastructure for monitoring services? If so—is that API available?**

**What is the execution environment (“MacBook”; should I assume a particular JVM or Python version, or Docker, or anything else?)**

> Assume we are building a new system. Please provide your suggestion.

**Are we building a proof of concept or production-ready code?**
That is, how much runway do we have? A fully-fleshed-out service might take a week; a skeleton demonstrating the design as a PoC might take until Monday.

> A PoC is sufficient. Please do mention your assumptions and known issues this design may have.

## Requirements & Assumptions

* This is a proof of concept, but it should be scaleable to production rates:
    * In a large enterprise, basic customer data retrieval needs to scale linearly to millions of retrievals per hour
    * Properly-defined user interfaces will usually allow for an initial retrieval latency of about 500ms, with more 
      tolerance for ancillary data such as customer service records.
    
* The initial deployment will be internal, but the goal is to be able to:
    * offer this as a service to third parties
    * deliver this as installable software to large enterprises

* To deliver this feature to a SaaS environment, the system must support multi-tenant behavior, built in from the start
    * I have back-fit multi-tenant behavior; it's terribly difficult to do, and the consequences of tenant data-bleedthrough are dire
    * I have also deployed multi-tenant systems by creating a new instance for each tenant; the costs will put you out of business.
    * this drives tenant ID as a key to nearly every retrieval or search, baked in to the core of the service

* Record search is *the* core competence
    * Customer data is read-mostly
    * CRM systems, especially at the customer service endpoint, must be able to retrieve customer records 
      using a wide variety of keys, e.g. a service record ID, partial customer name, customer ID, partial PII such 
      as zip codes or street addresses, and so on. The list of retrieval keys will change over time and become more 
      complex over time; the system should support extending the key store and rapid interactive results narrowing.
        * assume tens of billions of records, with hundreds of millions of records per tenant 
         (though typical tenants will be much smaller). 
        * assume data sharding by tenant is feasible, but also note that some tenants will have peak customer counts, 
        so account for that in the storage design.

* Security: enterprises have wildly varying security models, but they are most typically role-based. Each logical
  subdivision of data will require separate role-based permissions, and graceful degradation of results based on
  the caller's role(s). The base implementation needs to support:
    * declarative support for multiple roles
    * dynamic role configuration and mapping to APIs
    * pluggable, stacked support for AuthN/AuthZ schemes
       * dynamic client authorization, as near-term applications are likely to include user interfaces, which will
        typically only offer OAUTH2 and username/password via LDAP for authentication.
       * support for other auth schemes, e.g. client certificates, for app-to-app communication

* Since this is green-field development, some implementation decisions are simplified:
    * JSON over HTTPS satisfies the principle of least astonishment: this is possibly the most widely-implemented REST transport.
    * Strict REST adherence (rather than RPC-over-HTTP) makes it easier for clients to code to the API
    * The API should be described in one of the standard API definition languages so that clients can leverage code generation.
      Swagger (OpenAPI) and WSDL are the mature ones; Swagger has the most current mind-share and is therefore the choice.
    * Language selection:
        * lots of available tools and frameworks,
        * blazingly fast,
        * plenty of developers are available who know it,
        * strongly typed, which helps minimize runtime errors,
        * JVM (Java, Scala, Jython, JRuby, Groovy, Cloture, Kotlin) or Python
            * I don't actually know most of these, so that's kindof a detriment
            * The developer pool for Scala, Cloture, and Kotlin is small
            * Ruby is duck-typed, which, while "technically strong", tends to allow type errors to intrude
    * Monitoring: lift a page from Amazon and use a standard-structured log which can be ingested into a metrics service.
        * separates concerns
        * easy to implement; logging is well-defined and very fast in most languages
     * The whole service should be containerized so that container management frameworks can autoscale.

* The service should launch quickly and redeploy quickly. This dominates the cost of development.
  Under 2 seconds per round trip if possible.

* The unit testing framework should also support live full-stack tests, to simplify the deployment pipeline and 
  reduce the required skill set for developers.

# Design
Since I'm doing this in the context of an interview, a full decision analysis, which would require a week or 
  so of effort, isn't appropriate. In the "real world", I would survey the market for tools and emerging standards,
  build a list of evaluation criteria, both required and desired, and evaluate options against those criteria using
  a modified Kepner-Tregoe decision analysis.
  
The only languages that meet all the requirements are Java, Groovy, and Python. I am most familiar with Java, so 
  I will arbitrarily choose that.
  
There are a bunch of fully-fleshed-out microservice frameworks and tools available, and there is one I actively 
  avoid (Spring, because it's so slow and the DI framework encourages poor design from junior developers). There is 
  one I've been dying to try--Quarkus (<https://quarkus.io/>)--and not only does it cover all the requirements above, 
  but they have covered some things I haven't thought of, so I think it's a reasonable fit. Quarkus documentation is
  extensive and includes architecture and design documentation.

## Access and Roles
CRM access requirements vary greatly by enterprise. This PoC implements basic auth for expedience, and a small
number of roles as described below. The problem statement mentions several categories of information: profile data,
contact information, service case history, and "confidential" information. For this PoC, data are separated into 
3 categories:

1. PII: customer name, contact information, and other personally-identifying information
2. internal business data, e.g. profile data and service cases
3. confidential information

In a fully-fleshed-out CRM, a rich set of roles would be available; in this instance, we use just 3 roles:

* piiEnabled - able to access PII
* reader - able to access internal business data
* admin - able to access all categories of data

For consistency, a fourth "confidentialEnabled" role might have been implemented; I honestly didn't think of
it until the final review of this documentation.

In order to demonstrate multiple approaches, confidential information is masked rather than prohibiting retrieval
entirely.

In a real system, a richer object structure would allow for composition by role.

## Objects

There are two top-level data objects: Customer and ServiceCase; Customer embeds some confidential data directly,
and also has some client sub-structure (e.g. "ContactInfo"). ServiceCase points to Customer, and readers can
retrieve them, but can not retrieve Customer. Customer knows how to mask confidential data.

Batch results are returned in subclasses of ListPage to support delivering pagination information to the client.

For proper logging and tracing, all return objects should include a request ID. This isn't implemented, but in
a nod to the requirement, ListPage includes that object.

Throughout this code, persistent object IDs are presented as strongly-typed long values paired with a tenant ID.

![Objects](http://www.plantuml.com/plantuml/png/ZP91Rzim38Nl_XL4JYtG1DjrA88EsGO4ABgXJM-zq1OTQvCfJwGM55t_-sH5COpaieiiaq_lKHzq9uf1O--gng48RAAexod0MmK0zvxtOCsuVMijEVCE3zWJS5h6ufC6cnGYF9VSMz7RZMV5HhVS-ZKqnv-nKRrN9Mgco6dpkB9loZJRN9i2ldBi0OV1sGPhwwo-mjN1Xrytq2Nr3P0sg-jyuhCnWKG0ZzzSsv6ZeVDCaDSq47ib27RVwISOedCmDdqBsX7qoAylB8fiC9YJ4VZw9pNwmeR4xbcW3RxF-WFLLJLHN7nSmDBu0ozXmOlvbKyKdYZyiGrjKEW_AHJeWyE6EkyCXMy-F1AwOfNT4sMiHKDAWsFG2Fy7xkbsjov1py_RBn3eToJHucrPKqMYqusFh7FgkTVQ8FgO1TzvrraP7U7gmx968NCzUJYyb5sO3Qdu4FkQmfb3xRqZv9J6wAQZumNIy6M-ixTK46Nn1TXShUOQW5KSLd_N4vNgJ7xHloCsiNV_0000)

(edit with <https://tinyurl.com/y3qswwml>)

I have not yet thought through component design, but the current rough hierarchy is:

1. Service API implementation
2. Create/Retrieve/Update/Delete DAOs
3. domain objects (shown above)

A content verification layer needs to thread through there somewhere to ensure that access controls are respected.

## API
This API is kind-of a first cut. There is a lot of discussion in REST communities about the differences
between search and filter, for example. There is no reason we can't refine this design, but it expresses
the main ideas I want to get across, so treat it as a starting point.

Tenant ID is inferred from authentication information; we do this so as not to expose tenant ID to customers.
This may require a separate service to fully implement; in particular, we would want to be able to control
tenant access as well as allowing tenants to control their own credential system.

* GET .../customer/{customer\_id}
    * returns a single Customer object, masked i.a.w. the caller's role. Hidden fields are masked rather than removed.
* POST .../customer
    * inserts a new customer; returns a customer ID
* PUT .../customer/{customer\_id}
    * modifies customer with ID {customer\_id}

* GET .../customers/{filter_id}
    * returns a paged list of Customers, wrapped in an CustomerPage
    * filters are defined below; a filter is a first-class defined object
* POST .../customers
    * the body defines a filter; the filter language will likely evolve quickly, but for now,
      comprises a JSON object comprising either a list of customer IDs, or a set of 
      name-value pairs defining the search, e.g.: ```{"field1":"field1_value","field2":field2_value}```
    * this method returns a filter ID (201); alternately, could redirect to the GET.

* GET .../service\_case/{service\_case\_id}
    * returns a single ServiceCase
* GET .../servicecases/{filter_id}?page={pageNumber}
    * returns a paged list of ServiceCases, wrapped in an ServiceCasePage
    * the optional page parameter provides paging support. this is a starting point for a more
      flexible paging query language.
* POST .../service_cases
    * the body defines a filter; the filter language will likely evolve quickly, but for now,
      comprises a JSON object containing the customer ID.
    * this method returns a filter ID and status 201.

Service cases are assumed to be managed by another service (e.g. a ticketing system), so no creation
API is provided. The customer ID concept may differ between the ticket system and the customer
system, so that mapping will probably need to be stored as part of the customer record.

## Data Store

This is arguably the key component, but we can reduce this to an API for now.

Flexible query performance is a key goal; for this, a key/value pair datastore such as GCS Memorystore, Redis,
AWS ElastiCache, or ElasticSearch can provide great performance and flexibility; adding an index is a 
straightforward, scriptable activity. Such a datastore allows us to query for an object ID, then use that 
ID to retrieve the full record. Integrated indices such as you'd get on a relational database or with an 
index in an object datastore are less flexible and more expensive to implement.

Since the bulk of the content is not indexed, and the key/value store emits an object ID, the bulk Customer
store can be an object store such as AWS's DynamoDB, Mongo, GSC's Datastore, and so on. A small number of
indices (e.g. various IDs) can be directly indexed.

One might suspect that a 2-step retrieval (search for ID, then retrieve) might cause unacceptable latency,
but search results are typically available in < 10ms from these sources, so achieving an overall query
latency of <500ms is will within the capabilities of current systems.

## Deployment

In production, roughly as shown below. Green components are implemented; other components are stubbed 
inside the DAOs that provide interfaces to them.

![Deployment](http://www.plantuml.com/plantuml/png/VP9HIyCm483VyokE-Q8FXJwMFSXWC10SWXLyc4NIvBh5jPTvkQf3_E-cswZLRRurkQ_VNUvk4aIpj4sj5jCsY6-GS-GtPx20YoohgYnJv0MXnah6u2ZoP3203i3usZf6-qJYeK4KgBssOB7HP9MoMdIf0yBeXYn-G2wUSGIV2lfKi8iKeBw6-5tvWaPEyM6FXhMZZMRTW2D1hhJ13eDIc_MIVKjswclF_GDYnBD5FjocwMoMffteyWhCGGr90aSj60BXB17styz61pn3gSKZbjq9kOX4Bprt7_u6VkNucGZu2dfm37b-2uBSEDBYFAdZeIRMlrDoFUZe_ynWIgTiaNfmPetYQ0dvDWWsnN1V3cL3LpQiNFoRjwSyx9OhYR8v2ZiJu1utddTIJHRcdfnuZiT2JP1iNDTl)

edit: <https://tinyurl.com/108eilbu> 

## Notes
* login information is found in application.properties
* paging is not implemented; if you supply a page number, it will be populated in the result object just for show
* If you test with a browser, clear cookies between requests, because basic login is generally cached. I have mostly
  tested with Postman and unit tests
* search strategy might return summaries to allow for refining the search
* the entire tenant system might need its own service; the
   current API is really a stub, and should be thought out 
   before the system gets much larger; backfitting tenants is hard.
* implement
    * test coverage
    * integration tests; separate from unit tests
    * some kind of lint system
    * a pipeline
    * metrics & logging
    * a real authentication and authorization subsystem
* the data models are stubs and need some real analysis
* the default error handling is still in place; this exposes a lot of internal detail inappropriately.

