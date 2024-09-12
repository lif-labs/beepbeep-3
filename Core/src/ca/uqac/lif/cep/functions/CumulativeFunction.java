/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2022 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.functions;

import java.util.ArrayList;
import java.util.List;

/**
 * A function with memory.
 *
 * @author Sylvain Hallé
 * @since 0.1
 */
public class CumulativeFunction<T> extends UnaryFunction<T, T> {
    /**
     * The last value returned by the function
     */
    private T m_lastValue;

    /**
     * The start value of the function
     */

    private T m_startValue;

    /**
     * The stateless binary function to apply on each call
     */
    private BinaryFunction<T, T, T> m_function;

    /**
     * Instantiates a new cumulative function
     *
     * @param function The function to cumulate
     */
    public CumulativeFunction(BinaryFunction<T, T, T> function) {
        // If no start value is provided, we use the start value of the function that gived to the CumulativeFunction constructor
        this(function, function.getStartValue());
    }

    /**
     * Instantiates a new cumulative function with a start value
     *
     * @param function   the function to cumulate
     * @param startValue the start value of the function
     */
    public CumulativeFunction(BinaryFunction<T, T, T> function, T startValue) {
        super(function.getInputTypeLeft(), function.getOutputType());
        m_function = function;
        // We store the start value in case of reset
        m_startValue = startValue;
        m_lastValue = startValue;
    }

    @Override
    public T getValue(T x) {
        if (m_lastValue == null) {
            // If the function did not provide a start value, use the
            // first given argument as the start value
            m_lastValue = x;
            return x;
        }
        T value = m_function.getValue(m_lastValue, x);
        m_lastValue = value;
        return value;
    }

    @Override
    public void reset() {
        m_lastValue = this.m_startValue;
    }

    @Override
    public CumulativeFunction<T> duplicate(boolean with_state) {
        CumulativeFunction<T> cf = new CumulativeFunction<T>(m_function.duplicate(with_state));
        if (with_state) {
            cf.m_lastValue = m_lastValue;
        }
        return cf;
    }

    /**
     * @since 0.10.2
     */
    @Override
    public Object printState() {
        List<Object> list = new ArrayList<Object>(2);
        list.add(m_function);
        list.add(m_lastValue);
        return list;
    }

    /**
     * @since 0.10.2
     */
    @SuppressWarnings("unchecked")
    public CumulativeFunction<T> readState(Object o) {
        List<Object> list = (List<Object>) o;
        BinaryFunction<T, T, T> func = (BinaryFunction<T, T, T>) list.get(0);
        CumulativeFunction<T> cf = new CumulativeFunction<T>(func);
        cf.m_lastValue = (T) list.get(1);
        return cf;
    }

    /**
     * @return The last value
     * @since 0.11
     */
    /*@ pure @*/
    public T getLastValue() {
        return m_lastValue;
    }

}
