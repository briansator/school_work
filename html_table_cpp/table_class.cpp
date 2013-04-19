/*
table_class.cpp

Brian Sator
CS 320
Assignment #2
Copyright (c) 2013 Brian Sator. All Rights Reserved
*/


#include "table_class.h"
using namespace std;

html_table::html_table(int rows, int cols){
	this->num_rows = rows;
	this->num_cols = cols;
    std::vector<std::vector<bool> > table_map_b(cols,std::vector<bool>(rows));
    std::vector<std::vector<string> > table_map_v(cols,std::vector<string>(rows));
    
    //initialize 2d vector
    for(int i=0; i<rows; i++){
        vector<string> vec;
        vector<bool> bool_vec;
        for(int z=0; z<cols; z++){
            string str;
            bool boolean = false;
            vec.push_back(str);
            bool_vec.push_back(boolean);
        }
        this->table_map_v.push_back(vec);
        this->table_map_b.push_back(bool_vec);
    }
	
}

html_table::~html_table(){}

string html_table::to_string(){
    string tableHTML = "<table>";
    
    for(int rowTracker=0; rowTracker<this->num_rows; rowTracker++){
        tableHTML += "<tr>";
        for(int colTracker=0; colTracker<this->num_cols; colTracker++){
            tableHTML += "<td>";
            if(this->table_map_b.at(rowTracker).at(colTracker) == true){
                tableHTML += "<b>";
                tableHTML += this->table_map_v.at(rowTracker).at(colTracker);
                tableHTML += "</b>";
            }else{
                tableHTML += this->table_map_v.at(rowTracker).at(colTracker);
            }
            tableHTML += "</td>";
        }
        tableHTML += "</tr>";
    }
    
    tableHTML += "</table>";
    
    return tableHTML;
}

void html_table::add_rows(int number){
	this->num_rows = this->num_rows+number;
    
    this->table_map_b.resize(this->num_cols,vector<bool>(this->num_rows));
    this->table_map_v.resize(this->num_cols,vector<string>(this->num_rows));

    
}

void html_table::add_cols(int number){
    this->num_cols = this->num_cols+number;
    
    this->table_map_b.resize(this->num_cols,vector<bool>(this->num_rows));
    this->table_map_v.resize(this->num_cols,vector<string>(this->num_rows));
    
    
}

void html_table::mark_bold(int row, int col){
    table_map_b.at(row).at(col) = true;
 }

void html_table::put(int row, int col, int data){
    stringstream convert;
    convert << data;
	table_map_v.at(row).at(col) = convert.str();
}

void html_table::put(int row, int col, string data){
	table_map_v.at(row).at(col) = data;
}

/*void html_table::put(int row, int col, obj data){
 
 }*/


//here is our function to provide the html table output
string gradePage(string name){
    
    //start html string
    string htmlOutput = "<html lang='en'><head><meta http-equiv='content-type' content='text/html; charset=utf-8'><title>Sample Output</title></head><body>";
    
    //fetch student data from grades.txt
    ifstream gradesFile;
    gradesFile.open("/Users/Brian/Documents/table_class/table_class/grades.txt");
    int colCount = 0;
    if(!gradesFile){
        cout << "Unable to open grades.txt.";
    }
    
    //determine row data
    string rowString;
    vector<string> table_rows;
    while(getline(gradesFile, rowString)){
        //get next line and add to vector<string>
        table_rows.push_back(rowString);
    }
    gradesFile.close();
    
    //determine column data
    string firstRow = table_rows.at(0);
    stringstream stream(firstRow);
    string rowWords;
    while(getline(stream,rowWords,',')){
        colCount++;
    }
    
    //organize raw data for table rows after MaxPoints Row
    int studentRow;
    vector<vector<int>> flipped_columns(table_rows.size()-2, vector<int>(colCount-1));
    int flip_x = 0;
    for(int i=1; i<table_rows.size()-1; i++){
        //we start index at 1 to ignore first row of titles
        vector<int> assignmentScores;
        string thisRow = table_rows.at(i);
        stringstream stream(thisRow);
        string thisWord;
        int colTracker = 0;
        while(getline(stream,thisWord,',')){
            //check each column value, ignore first index (name)
            if(colTracker==0){
                //first col, check name to determine if req student row
                if(thisWord == name){
                    studentRow = i;
                }
            }else{
                //not the first column with names, extract score value
                int score;
                stringstream(thisWord) >> score;
                assignmentScores.push_back(score);
                flipped_columns.at(colTracker-1).at(flip_x) = score;
            }
            colTracker++;
        }
        flip_x++;
        
        
        
    }
    
   
    
    //create html_table object
    html_table table(table_rows.size()+1, colCount);
    
    //grab the first row that constitutes the header of the table
    stringstream first_row(table_rows[0]);
    string table_words;
    int column_counter=0;
    
    while(getline(first_row,table_words,',')){
        table.put(0,column_counter,table_words);
        column_counter++;
    }
    column_counter = 0;
    
    //grab the second row which is the student's row provided in the "name" parameter
    stringstream second_row(table_rows[studentRow]);
    while(getline(second_row,table_words,',')){
        table.put(1,column_counter,table_words);
        column_counter++;
    }
    column_counter = 0;
    
    //grab the third row which is the max points row
    stringstream third_row(table_rows[table_rows.size()-1]);
    while(getline(third_row,table_words,',')){
        table.put(2,column_counter,table_words);
        column_counter++;
    }
    column_counter = 0;
    
    
    //here we input the remaining row values
    int start_col = 1;
    int start_row = 3;
    //TODO temp output helper vector to see sort
    for(int i=0; i<flipped_columns.size(); i++){
        int start_line = table_rows.size();
        vector<int> sorted = flipped_columns.at(i);
        sort(sorted.begin(), sorted.end());
        for(int z=0; z<sorted.size(); z++){
            stringstream convert;
            convert << sorted.at(z);
            table.put(start_line,start_col, convert.str());
            start_line--;
        }
        stringstream convert2;
        convert2 << start_col;
        table.put(start_row,0, convert2.str());
        start_col++;
        start_row++;
    }

    
    
    //append html_table to_string result to htmlOutput
    htmlOutput += table.to_string();
    
    //add closing tags
    htmlOutput += "</body></html>";
    
    return htmlOutput;
}

int main(){
    
    //prompt for student name to grade
    string student;
    cout << "Please enter student to grade: ";
    cin >> student;
    
    //perform grade page
    string gradeHTML = gradePage(student);
    
    //output html
    cout << gradeHTML;
    
	return 0;
}

