# reactivity benchmark

This is a benchmark bettween 2 frameworks : 

   **1_ Akka Stream :** A reactive framework with Non Blocking BackPressure control
                        
   **2_ FlowControl :** A multithread with Blocking BackPressure control
        
## Expected Result

|Benchmark                                                                       |Mode   |Cnt |Score   |Error  |Units
---------------------------------------------------------------------------------|------ |----|--------|-------|-----
|CountWordInFileBK.count_using_Akka_Stream_Library                               |avgt   |6   |294,248 |57,009 |ms/op
|CountWordInFileBK.count_using_MultiThread_With_A_Blocking_BackPressure_Library  |avgt   |6   |62,262  |12,478 |ms/op

This very basic benchmark demonstrates a gain of performance of **79%** using a simple MultiThread Library 

**Gain of 79%** = (1-(62,262/294,248))*100
