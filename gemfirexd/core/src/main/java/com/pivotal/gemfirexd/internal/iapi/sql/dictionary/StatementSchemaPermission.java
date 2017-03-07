/*

   Derby - Class com.pivotal.gemfirexd.internal.iapi.sql.dictionary.StatementSchemaPermission

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

	  http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package com.pivotal.gemfirexd.internal.iapi.sql.dictionary;

import com.gemstone.gemfire.internal.i18n.LocalizedStrings;
import com.pivotal.gemfirexd.internal.engine.Misc;
import com.pivotal.gemfirexd.internal.iapi.error.StandardException;
import com.pivotal.gemfirexd.internal.iapi.reference.SQLState;
import com.pivotal.gemfirexd.internal.iapi.services.sanity.SanityManager;
import com.pivotal.gemfirexd.internal.iapi.sql.conn.Authorizer;
import com.pivotal.gemfirexd.internal.iapi.sql.conn.LanguageConnectionContext;
import com.pivotal.gemfirexd.internal.iapi.sql.dictionary.SchemaDescriptor;
import com.pivotal.gemfirexd.internal.iapi.store.access.TransactionController;

/**
 * This class describes a schema permission required by a statement.
 */

public class StatementSchemaPermission extends StatementPermission
{
	/**
	 * The schema name 
	 */
	private String schemaName;
	/**
	 * Authorization id
	 */
	private String aid;  
	/**	 
	 * One of Authorizer.CREATE_SCHEMA_PRIV, MODIFY_SCHEMA_PRIV,  
	 * DROP_SCHEMA_PRIV, etc.
	 */ 
	private int privType;  

	public StatementSchemaPermission(String schemaName, String aid, int privType)
	{
		this.schemaName = schemaName;
		this.aid 	= aid;
		this.privType	= privType;
	}

	/**
	 * @see StatementPermission#check
	 */
	public void check( LanguageConnectionContext lcc,
					   String authid,
					   boolean forGrant) throws StandardException
	{
		DataDictionary dd =	lcc.getDataDictionary();
		TransactionController tc = lcc.getTransactionExecute();
	
		switch ( privType )
		{
			case Authorizer.MODIFY_SCHEMA_PRIV:
			case Authorizer.DROP_SCHEMA_PRIV:
				SchemaDescriptor sd = dd.getSchemaDescriptor(schemaName, tc, false);
				// If schema hasn't been created already, no need to check
				// for drop schema, an exception will be thrown if the schema 
				// does not exists.
				if (sd == null)
					return;

				if (!authid.equals(sd.getAuthorizationId())) {
					if (schemaName.equalsIgnoreCase("SNAPPYSYS_INTERNAL")) {
						Misc.getI18NLogWriter().info(LocalizedStrings.DEBUG, "ABS schema check failed: " +
								authid + ", " + sd.getAuthorizationId(), new Exception());
					}
					throw StandardException.newException(
							SQLState.AUTH_NO_ACCESS_NOT_OWNER, authid, schemaName);
				}
				if (schemaName.equalsIgnoreCase("SNAPPYSYS_INTERNAL")) {
					Misc.getI18NLogWriter().info(LocalizedStrings.DEBUG, "ABS schema check passed: " +
							authid + ", " + sd.getAuthorizationId(), new Exception());
				}
				break;
			
			case Authorizer.CREATE_SCHEMA_PRIV:
				// Non-DBA Users can only create schemas that match their authid
				// Also allow only DBA to set authid to another user
				// Note that for DBA, check interface wouldn't be called at all
				if (schemaName.equalsIgnoreCase("SNAPPYSYS_INTERNAL")) {
					Misc.getI18NLogWriter().info(LocalizedStrings.DEBUG, "ABS create schema check. authid " +
							authid + ", aid " + aid);
				}
				if ( !schemaName.equals(authid) ||
						(aid != null && !aid.equals(authid)) )
					throw StandardException.newException(
						SQLState.AUTH_NOT_DATABASE_OWNER, authid, schemaName);
				break;
			
			default:
				if (SanityManager.DEBUG)
				{
					SanityManager.THROWASSERT(
							"Unexpected value (" + privType + ") for privType");
				}
				break;
		}
	}

	/**
	 * Schema level permission is never required as list of privileges required
	 * for triggers/constraints/views and hence we don't do any work here, but
	 * simply return null
	 * 
	 * @see StatementPermission#check
	 */
	public PermissionsDescriptor getPermissionDescriptor(String authid, DataDictionary dd)
	throws StandardException
	{
		return null;
	}
}
