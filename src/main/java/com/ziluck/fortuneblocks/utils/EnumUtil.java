package com.ziluck.fortuneblocks.utils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Utils for working with enums. Most of the code was taken EssentialsX.
 *
 * @author Michael Ziluck
 */
public class EnumUtil
{
    /**
     * Return a set containing <b>all</b> fields of the given enum that maths one of the provided
     * names.
     *
     * @param enumClass The class to search through
     * @param names     The names of the fields to search for
     * @param <T>       The enum to search through
     *
     * @return All matching enum fields
     */
    public static <T extends Enum> Set<T> getAllMatching(Class<T> enumClass, String... names)
    {
        Set<T> set = new HashSet<>();

        for (String name : names)
        {
            try
            {
                Field enumField = enumClass.getDeclaredField(name);

                if (enumField.isEnumConstant())
                {
                    set.add((T) enumField.get(null));
                }
            }
            catch (NoSuchFieldException | IllegalAccessException ignored)
            {
            }
        }

        return set;
    }
}
