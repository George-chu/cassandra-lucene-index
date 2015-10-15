/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.cassandra.lucene.search.sort;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.cassandra.db.marshal.UTF8Type;
import org.junit.Test;

import com.stratio.cassandra.lucene.schema.column.Column;
import com.stratio.cassandra.lucene.schema.column.Columns;

/**
 * @author EduardoAlonso {@literal <eduardoalonso@stratio.com>}
 */
public class SortTest {

    @Test
    public void testBuildEmpty() {
        List<SortField>  emptyList= new ArrayList<>();
        Sort sort= new Sort(emptyList);
        assertEquals("Sort build with empty SortField List must return a list whit size 0 but returns: " + sort
                .getSortFields().size(), sort.getSortFields().size(), 0);
    }

    @Test
    public void testBuildDefaults() {
        SortField sortField = new SortField("field", null);
        List<SortField> oneElementList= new ArrayList<>();
        oneElementList.add(sortField);
        Sort sort= new Sort(oneElementList);
        List<SortField> result= sort.getSortFields();

        assertEquals("Sort build with one element SortField List must return a list with size 1 but returns: "
                + result.size(), result.size(), 1);

        assertEquals("Sort build with 1 sort Field does not returns the same SortField ", sortField, result.get(0));

    }
    @Test
    public void testBuild2() {
        SortField sortField = new SortField("field", false);
        SortField sortField2 = new SortField("field_2", true);
        List<SortField> twoElementList= new ArrayList<>();
        twoElementList.add(sortField);
        twoElementList.add(sortField2);
        Sort sort= new Sort(twoElementList);
        List<SortField> result= sort.getSortFields();

        assertEquals("Sort build with two elements SortField List must return a list with size 2 but returns: "
                        + ""+result.size(),result.size(),2);
        assertEquals("Sort build with two SortField does not returns the same SortFields ", twoElementList, result);

    }
    @Test
    public void testIterator() {
        SortField sortField = new SortField("field", false);
        SortField sortField2 = new SortField("field_2", true);
        SortField sortField3 = new SortField("field_3", true);
        List<SortField> sortFields= new ArrayList<>();
        sortFields.add(sortField);
        sortFields.add(sortField2);
        sortFields.add(sortField3);
        Sort sort= new Sort(sortFields);
        int numElems=0;
        for (SortField sortF: sort) {
            numElems+=1;
            if ((!sortF.equals(sortField)) && (!sortF.equals(sortField2)) && (!sortF.equals(sortField3))) {
                assertTrue("Sort iterator not returning all the sortFields",true);
            }
        }

        assertEquals("Sort build with 3 elements iterator must return 3 elems but returns : "+numElems,numElems,3);
    }

    @Test
    public void testComparator() {
        SortField sortField = new SortField("field", false);
        SortField sortField2 = new SortField("field_2", false);
        SortField sortField3 = new SortField("field_3", false);
        List<SortField> sortFields= new ArrayList<>();
        sortFields.add(sortField);
        sortFields.add(sortField2);
        sortFields.add(sortField3);
        Sort sort= new Sort(sortFields);

        Comparator<Columns> comparator = sort.comparator();


        Column<String> column = Column.fromComposed("field", "a", UTF8Type.instance, false);
        Column<String> column2 = Column.fromComposed("field", "b", UTF8Type.instance, false);
        Column<String> column3 = Column.fromComposed("field", "c", UTF8Type.instance, false);
        Column<String> column4 = Column.fromComposed("field", "d", UTF8Type.instance, false);
        Column<String> column5 = Column.fromComposed("field", "e", UTF8Type.instance, false);

        Column<String> columnField2_1 = Column.fromComposed("field_2", "a", UTF8Type.instance, false);
        Column<String> columnField2_2 = Column.fromComposed("field_2", "b", UTF8Type.instance, false);
        Column<String> columnField2_3 = Column.fromComposed("field_2", "c", UTF8Type.instance, false);
        Column<String> columnField2_4 = Column.fromComposed("field_2", "d", UTF8Type.instance, false);
        Column<String> columnField2_5 = Column.fromComposed("field_2", "e", UTF8Type.instance, false);


        Column<String> columnField3_1 = Column.fromComposed("field_3", "e", UTF8Type.instance, false);
        Column<String> columnField3_2 = Column.fromComposed("field_3", "d", UTF8Type.instance, false);
        Column<String> columnField3_3 = Column.fromComposed("field_3", "c", UTF8Type.instance, false);
        Column<String> columnField3_4 = Column.fromComposed("field_3", "b", UTF8Type.instance, false);
        Column<String> columnField3_5 = Column.fromComposed("field_3", "a", UTF8Type.instance, false);


        Columns columns1 = new Columns().add(column).add(columnField2_1).add(columnField3_1);
        Columns columns2 = new Columns().add(column2).add(columnField2_2).add(columnField3_2);
        Columns columns3 = new Columns().add(column3).add(columnField2_3).add(columnField3_3);
        Columns columns4 = new Columns().add(column4).add(columnField2_4).add(columnField3_4);
        Columns columns5 = new Columns().add(column5).add(columnField2_5).add(columnField3_5);


        assertEquals("SortField columns comparator is wrong", -1, comparator.compare(columns1, columns2));
        assertEquals("SortField columns comparator is wrong", -2, comparator.compare(columns1, columns3));
        assertEquals("SortField columns comparator is wrong", -3, comparator.compare(columns1, columns4));
        assertEquals("SortField columns comparator is wrong", -4, comparator.compare(columns1, columns5));
        assertEquals("SortField columns comparator is wrong", -1, comparator.compare(columns2, columns3));
        assertEquals("SortField columns comparator is wrong", -2, comparator.compare(columns2, columns4));
        assertEquals("SortField columns comparator is wrong", -3, comparator.compare(columns2, columns5));
        assertEquals("SortField columns comparator is wrong", -1, comparator.compare(columns3, columns4));
        assertEquals("SortField columns comparator is wrong", -2, comparator.compare(columns3, columns5));
        assertEquals("SortField columns comparator is wrong", -1, comparator.compare(columns4, columns5));

        assertEquals("SortField columns comparator is wrong", 1, comparator.compare(columns2, columns1));
        assertEquals("SortField columns comparator is wrong", 2, comparator.compare(columns3, columns1));
        assertEquals("SortField columns comparator is wrong", 3, comparator.compare(columns4, columns1));
        assertEquals("SortField columns comparator is wrong", 4, comparator.compare(columns5, columns1));
        assertEquals("SortField columns comparator is wrong", 1, comparator.compare(columns3, columns2));
        assertEquals("SortField columns comparator is wrong", 2, comparator.compare(columns4, columns2));
        assertEquals("SortField columns comparator is wrong", 3, comparator.compare(columns5, columns2));
        assertEquals("SortField columns comparator is wrong", 1, comparator.compare(columns4, columns3));
        assertEquals("SortField columns comparator is wrong", 2, comparator.compare(columns5, columns3));
        assertEquals("SortField columns comparator is wrong", 1, comparator.compare(columns5, columns4));

        assertEquals("SortField columns comparator is wrong", 0, comparator.compare(columns1, columns1));
        assertEquals("SortField columns comparator is wrong", 0, comparator.compare(columns2, columns2));
        assertEquals("SortField columns comparator is wrong", 0, comparator.compare(columns3, columns3));
        assertEquals("SortField columns comparator is wrong", 0, comparator.compare(columns4, columns4));
        assertEquals("SortField columns comparator is wrong", 0, comparator.compare(columns5, columns5));
    }
}