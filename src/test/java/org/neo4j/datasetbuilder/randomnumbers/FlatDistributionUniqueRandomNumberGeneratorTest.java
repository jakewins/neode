package org.neo4j.datasetbuilder.randomnumbers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.neo4j.datasetbuilder.randomnumbers.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;

import java.util.List;
import java.util.Random;

import org.junit.Test;

public class FlatDistributionUniqueRandomNumberGeneratorTest
{
    @Test
    public void shouldReturnListOfNumbers() throws Exception
    {
        // given
        RandomNumberGenerator generator = flatDistribution( new Random() ) ;

        // when
        List<Integer> results = generator.generate( 5, 1, 5 );

        // then
        assertTrue( results.contains( 1 ) );
        assertTrue( results.contains( 2 ) );
        assertTrue( results.contains( 3 ) );
        assertTrue( results.contains( 4 ) );
        assertTrue( results.contains( 5 ) );
    }

    @Test
    public void shouldAllowForZeroNumbersToBeGenerated() throws Exception
    {
        // given
        RandomNumberGenerator generator = flatDistribution( new Random( ) ) ;

        // when
        List<Integer> results = generator.generate( 0, 0, 0, 1 );

        // then
        assertEquals( 0, results.size() );
    }

    @Test
    public void shouldReturnSingleNumber() throws Exception
    {
        // given
        RandomNumberGenerator generator = flatDistribution( new Random( ) ) ;

        // when
        int result = generator.generateSingle( 1, 1 );

        // then
        assertEquals( 1, result );
    }
}