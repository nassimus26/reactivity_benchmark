# Reactivity Benchmark

This is a basic benchmark bettween 2 frameworks : 

   **1_ [Akka Stream](https://github.com/akka/akka) :** A reactive, distributed, and resilient message-driven framework with Non Blocking BackPressure control
                        
   **2_ [FlowControl](https://github.com/nassimus26/FlowControl) :** A tiny multithread framework with Blocking BackPressure control
                
The test is about counting the number of a given word in a File
                
## Expected Result

|Benchmark                                                                       |Mode   |Cnt |Score   |Error  |Units
---------------------------------------------------------------------------------|------ |----|--------|-------|-----
|CountWordInFileBK.count_using_Akka_Stream_Library                               |avgt   |6   |2517,248 |57,009 |ms/op
|CountWordInFileBK.count_using_MultiThread_With_A_Blocking_BackPressure_Library  |avgt   |6   |495,262  |12,478 |ms/op

This basic benchmark demonstrates a gain of performance of **500%** using a tiny MultiThread Library 

**Gain of 500%** = (2517,248/495,262)*100

**NB :** This dosn't changes the fact that Akka Stream Library is the most powerfull reactive library that supports many interesting features :

        1_ Non Blocking BackPresuure
        2_ Resiliency  
        3_ Distriuted
        ...
