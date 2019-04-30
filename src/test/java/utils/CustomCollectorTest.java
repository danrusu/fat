package test.java.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;


public class CustomCollectorTest {


    @Test
    public void CustomToListCollectorTest() {
        

        Stream<Integer> intsStream =  List.of(1,2,3,4,5).stream().parallel().unordered();
        

       // Three parts needed for a custom collector: Supplier, Accumulator, Combiner
        
        Supplier<Set<Integer>> supplier = () -> new HashSet<Integer>();

        BiConsumer<Set<Integer>, Integer> accumulator = (set, i) -> set.add(i);

        BinaryOperator<Set<Integer>> combiner = (set1, set2) -> {
            set1.addAll(set2);
            return set1;
        };


        // Create custom collector (to list)
        
        Collector<Integer, Set<Integer>, Set<Integer>> customCollector = Collector.of(
                
                supplier,
                
                accumulator,
                
                combiner,
                
                Collector.Characteristics.IDENTITY_FINISH
         );
        
        System.out.println(intsStream.collect(customCollector));
    }

}
