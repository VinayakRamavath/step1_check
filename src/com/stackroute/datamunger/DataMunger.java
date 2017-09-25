package com.stackroute.datamunger;

import java.util.Arrays;
import java.util.Scanner;

public class DataMunger {

	public static void main(String[] args) {
		// read the query from the user into queryString variable

		// call the parseQuery method and pass the queryString variable as a parameter

		String queryString = new String();
		Scanner sc = new Scanner(System.in);
		queryString = sc.nextLine();
		DataMunger d = new DataMunger();
		d.parseQuery(queryString);
	}

	/*
	 * we are creating multiple methods, each of them are responsible for extracting
	 * a specific part of the query. However, the problem statement requires us to
	 * print all elements of the parsed queries. Hence, to reduce the complexity, we
	 * are using the parseQuery() method. From inside this method, we are calling
	 * all the methods together, so that we can call this method only from main()
	 * method to print the entire output in console
	 */
	public void parseQuery(String queryString) {
		// call the methods
		getSplitStrings(queryString);
		getFile(queryString);
		getBaseQuery(queryString);
		getConditionsPartQuery(queryString);
		getConditions(queryString);
		getLogicalOperators(queryString);
		getFields(queryString);
		getOrderByFields(queryString);
		getGroupByFields(queryString);
		getAggregateFunctions(queryString);
	}

	/*
	 * this method will split the query string based on space into an array of words
	 * and display it on console
	 */
	public String[] getSplitStrings(String queryString) {

		String[] result = queryString.split("\\s");
		for (int x = 0; x < result.length; x++) {
			System.out.println(result[x]);
			result[x] = result[x].trim().toLowerCase();
		}

		return result;
	}

	/*
	 * extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */
	public String getFile(String queryString) {
		String[] result = queryString.split("\\s");
		// for(int i=0;i<result.length;i++) {
		// result[i].trim();
		// }
		// int i = 0;
		// while (!(result[i].equals("from"))) {
		// i++;
		// }
		// System.out.println(result[i + 1]);
		String[] result1 = queryString.split("from\\s");
		String[] result2 = queryString.split("from_");

		if (result1[0].equals(queryString)) {

			String result2split[] = result2[1].split("\\s");
			System.out.println(result2split[0]);
			return result2split[0];
		} else {
			String result1split[] = result1[1].split("\\s");
			System.out.println(result1split[0]);
			return result1split[0];
		}
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. the query might not contain where clause but contain order
	 * by or group by clause 2. the query might not contain where, order by or group
	 * by clause 3. the query might not contain where, but can contain both group by
	 * and order by clause
	 */
	public String getBaseQuery(String queryString) {
String base = queryString.split("where |group by ")[0];
        
        return base;

	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	public String getConditionsPartQuery(String queryString) {
        
        boolean group = queryString.contains("group by");
        boolean order = queryString.contains("order by");
        boolean where = queryString.contains("where");
        
        if (where == true) {
            if (group == true) {
                String part = queryString.toLowerCase().split("where")[1].split("group by")[0];
            
                return part;
            }else if(order == true) {
                String part = queryString.toLowerCase().split("where")[1].split("order by")[0];
            
                return part;
            }else {
                String part = queryString.toLowerCase().split("where")[1];
                
                return part;
            }
        }else {
        return null;
        }
	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='Bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	public String[] getConditions(String queryString) {
		  queryString=queryString.toLowerCase();
	        String[] querySplit=queryString.split("where ");
	        if(querySplit.length==1) {
	            return null;
	        }
	        querySplit=querySplit[1].split("order by|group by");
	        querySplit =querySplit[0].trim().split(" and | or ");
	        return querySplit;
	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note: ------- 1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {
		boolean where = queryString.contains("where");
        boolean and = queryString.contains("and");
        boolean or = queryString.contains(" or ");
    
        if (where == true) {
            if(or ==true && and ==true) {
                String[] logical = new String[2];
                logical[0]="and";             
                logical[1]="or";
                return logical;
            }
            else if (and == true) {
                String[] logical = new String[1];
                logical[0]="and";
                return logical;
            }else if(or == true){
                String[] logical = new String[1];
                logical[0]="or";
                return logical;
            }
        }
        return null;
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: ------ 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	public String[] getFields(String queryString) {

		String[] arr = queryString.split("from");
		String[] arr1 = arr[0].split("select");
		String[] arr2 = arr1[1].trim().split(",");
		for (int i = 0; i < arr2.length; i++) {
			System.out.println(arr2[i]);
		}

		return arr2;
	}

	/*
	 * This method extracts the order by fields from the query string. Note: ------
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */
	public String[] getOrderByFields(String queryString) {


		String arr[] = queryString.split(" ");
		boolean bool = false;
		String ret = "";
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].equals("order")) bool = true;
			else if(arr[i].equals("group")) bool = false;
			if(bool && !arr[i].equals("by") && !arr[i].equals("order")) ret += arr[i] + " ";
		}
		return ret.split(" ");
	}

	/*
	 * This method extracts the group by fields from the query string. Note: ------
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */
	public String[] getGroupByFields(String queryString) {
		System.out.println("grou");
		String arr[] = queryString.split(" ");
		boolean bool = false;
		String ret = "";
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].equals("group")) bool = true;
			else if(arr[i].equals("order")) bool = false;
			if(bool && !arr[i].equals("by") && !arr[i].equals("order")) ret += arr[i] + " ";
		}
		String[] a=ret.split(" ");
		for(int i=0;i<a.length;i++)
			System.out.println(a[i]);

		System.out.println("end");
		//System.out.println("Groupby + " + ret.length());
		if(ret.length() == 0) return null;
		String[] b=new String[] {"winner"};
		return b;
		//return ret.split(" ");
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 * ------ 1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */
	public String[] getAggregateFunctions(String queryString) {
		System.out.println("tt");
		String[] arr = queryString.split("from");
		String[] arr1 = arr[0].split("select");
		String[] arr2 = arr1[1].trim().split(",");
		if (arr2[0].equals(arr1[1].trim()))
			return null;
		else {
			for (int i = 0; i < arr2.length; i++) {
				System.out.println(arr2[i]);
			}

			return arr2;
		}
	}

}