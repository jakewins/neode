package org.neo4j.datasetbuilder.randomnumbers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class BaseUniqueRandomNumberGenerator implements RandomNumberGenerator
{
    private final Random random;

    protected BaseUniqueRandomNumberGenerator( Random random )
    {
        this.random = random;
    }

    @Override
    public final List<Integer> generate( int minNumberOfResults, int maxNumberOfResults, int min, int max )
    {
        if ( maxNumberOfResults < minNumberOfResults )
        {
            throw new IllegalArgumentException(
                    "maxNumberOfResults must be greater than or equal to minNumberOfResults)" );
        }

        if ( max <= min )
        {
            throw new IllegalArgumentException(
                    "max must be greater than min" );
        }

        if ( minNumberOfResults < 0 )
        {
            throw new IllegalArgumentException( "minNumberOfResults must be greater than or equal to 0" );
        }

        if ( min < 0 )
        {
            throw new IllegalArgumentException( "min must be greater than or equal to 0" );
        }

        if ( (max - min) < (maxNumberOfResults - minNumberOfResults) )
        {
            throw new IllegalArgumentException(
                    "(maxNumberOfResults - minNumberOfResults) must be greater than (max - min)" );
        }

        int numberOfResults = (maxNumberOfResults == minNumberOfResults) ? maxNumberOfResults :
                minNumberOfResults + random().nextInt( maxNumberOfResults - minNumberOfResults );
        List<Integer> generatedNumbers = new ArrayList<Integer>( numberOfResults );
        int i = 0;
        int upTo = max - min;
        while ( i < numberOfResults )
        {
            int nextNumber = getNextNumber( min, upTo );
            if ( nextNumber >= min && nextNumber <= max && !generatedNumbers.contains( nextNumber ) )
            {
                generatedNumbers.add( nextNumber );
                i++;
            }
        }
        return generatedNumbers;
    }

    @Override
    public final List<Integer> generate( int numberOfResults, int min, int max )
    {
        return generate( numberOfResults, numberOfResults, min, max );
    }

    @Override
    public final int generateSingle( int min, int max )
    {
        if ( max < min )
        {
            throw new IllegalArgumentException(
                    "max must be greater than or equal to min" );
        }

        if ( min < 0 )
        {
            throw new IllegalArgumentException( "min must be greater than or equal to 0" );
        }

        int nextNumber = getNextNumber( min, max-min );
        while (nextNumber < min || nextNumber > max)
        {
            nextNumber = getNextNumber( min, max-min );
        }

        return nextNumber;
    }

    protected final Random random()
    {
        return random;
    }

    protected abstract int getNextNumber( int min, int upTo );
}