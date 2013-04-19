/*
table_class.h

Brian Sator
CS 320
Assignment #2
Copyright (c) 2013 Brian Sator. All rights reserved.
*/

#ifndef table_class_table_class_h
#define table_class_table_class_h

#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include <sstream>
#include <stdlib.h>
#include <algorithm>

using namespace std;

class html_table{
    
public:
	html_table(int rows, int cols);
    
	~html_table();
    
	string to_string();
    
	void add_rows(int number);
    
	void add_cols(int number);
    
	void mark_bold(int row, int col);
	
	void put(int row, int col, int data);
	
	void put(int row, int col, string data);
    
    //void put(int row, int col, obj data);
    
	
private:
    
	int num_rows;
	int num_cols;
	
	vector<vector<bool> > table_map_b;
	vector<vector<string> > table_map_v;
    
	
	
};



#endif
