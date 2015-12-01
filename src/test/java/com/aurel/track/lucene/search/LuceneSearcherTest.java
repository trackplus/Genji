/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.lucene.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery.TooManyClauses;
import org.apache.lucene.search.IndexSearcher;
import org.junit.Test;

public class LuceneSearcherTest {

	@Test
	public void test() throws TooManyClauses, ParseException {
		assertNotNull(LuceneSearcher.getLookupFieldSearchers());
	}
	
	@Test
	public void testfieldNameIndex(){
		assertNotNull(LuceneSearcher.fieldNameIndex("toBeProcessedString", "fieldName", 0));
	}
	
	@Test
	public void testgetOrFlagsArray(){
		BooleanClause.Occur[] bco = LuceneSearcher.getOrFlagsArray(5);
		assertEquals(5,bco.length);
		assertEquals( BooleanClause.Occur.SHOULD,bco[0]);	
	}
	
	@Test
	public void testgetFieldValueEmpty(){
		assertEquals("",LuceneSearcher.getFieldValue(""));
	}
	
	@Test
	public void testgetFieldValue(){
		assertEquals("queryString",LuceneSearcher.getFieldValue("queryString"));
	}
	
	@Test
	public void testgetIndexSearcherNull(){
		assertNotNull(LuceneSearcher.getIndexSearcher(0));
	}
	
	@Test
	public void testcloseIndexSearcherAndUnderlyingIndexReader(){
		IndexSearcher is = mock(IndexSearcher.class);
		LuceneSearcher.closeIndexSearcherAndUnderlyingIndexReader(is, "index");
		assertTrue(true);
	}
	
	@Test
	public void testcreateORDividedIDs(){
		Set<Integer> objectIDs = new HashSet<Integer>();
		assertEquals("",LuceneSearcher.createORDividedIDs(objectIDs));
	}
	
	@Test
	public void testcreateORDividedIDs2(){
		Set<Integer> objectIDs = new HashSet<Integer>();
		objectIDs.add(2);
		assertEquals("2",LuceneSearcher.createORDividedIDs(objectIDs));
	}
	
	@Test
	public void testcreateORDividedIDs3(){
		Set<Integer> objectIDs = new HashSet<Integer>();
		objectIDs.add(0);
		assertEquals("0",LuceneSearcher.createORDividedIDs(objectIDs));
	}

}
