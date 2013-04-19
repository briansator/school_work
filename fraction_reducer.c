/*
* Brian Sator
* CS320 - Program 1
* masc0916
* Due: 3/14/2013
*/

#include <stdio.h>
#include <stdlib.h>

//prototyped functions
int in_validate(const char *s);
void flushinstream(void);
void findvalues(void);
int findGCD(int a, int b);
int validatenumbers(void);
int is_negative(int numerator, int denominator);

//global variabls
int valid_input = 0;
char buffer1[30];
char buffer2[30];
int slash_position;
char num[30] = {'\0'};
char denom[30] = {'\0'};
char *numcheck = num;
char *denomcheck = denom;
int commondivisor;
int negative;
int negative_bottom = 0;


//main function
int main(void){

//variables for main function
char *yes = "y";
char *no = "n";
int progcontinue = 0;
int continuetest = 0;	

printf("Brian Sator, masc0916\n\n");//output program setup information

	for(;;){
		//at the beginning of each cycle we check to see if we need to run the main routine again
		if(progcontinue != 0){
			break;//exit loop
		}
		printf("Please enter a fraction to reduce: ");//prompt user
		scanf("%25s",buffer1);//read from input
		flushinstream();//flush the input stream
		if(in_validate(buffer1) == 1){//the input string is a valid fraction proceed with reduction
			int numtest = atoi(num);
			int denomtest = atoi(denom);
			negative = is_negative(numtest, denomtest);
			commondivisor = abs(findGCD(numtest,denomtest));
			numtest = abs(numtest/commondivisor);
			denomtest = abs(denomtest/commondivisor);
			if(negative == 0){//check if negative
				printf("The reduced fraction is: %d/%d\n\n",numtest, denomtest);
			}else{
				printf("The reduced fraction is: -%d/%d\n\n",numtest, denomtest);
			}
			numtest = 0;
			numtest = 0;
			
		}
		else{//the input string is not valid, start routine over to prompt for new input
			continue;
		}
		
		while(continuetest == 0){//here we check to see if user wants to reduce another fraction
			printf("Do you want to reduce another fraction(y/n)? ");
			scanf("%25s",buffer1);
			flushinstream();
			if(buffer1[0] == *no){//end program
				progcontinue = 1;
				continuetest = 1;
			}else if(buffer1[0] == *yes){//new fraction
				continuetest = 1;
			}
			else{//invalid input
				printf("Sorry, invalid input\n\n");
			}
		}
		continuetest = 0;
		memset(num,0,30);
		memset(denom,0,30);
	}
	
printf("Program Terminated\n");
return 0;//exit program
}

//validate the input
int in_validate(const char *s){
	if(strlen(s) > 23){//input string is too large
		return 0; // false
	}
	int i = s[0] == '-' ? 1 : 0;
	for(; i < strlen(s); i++){
		if(isdigit(s[i]));//check if next char is a digit
		else if(s[i] == '/'){//check if slash
			slash_position = i;//set the position of the slash
		}else if(s[i] == '-' && i == (slash_position+1));
		else{
			printf("Invalid characters in numerator or denominator\n\n");
			return 0;
		}
	}
	if(slash_position == 0 || slash_position == (strlen(s)-1)){//check that the slash isnt the first or last char
		printf("Your slash is in an innapropriate location\n\n");
		return 0;
	}
	findvalues();//get the numerator and denominator
	if(validatenumbers() == 0){//check to make sure the numerator and denominator are valid numbers
		printf("Numerator or denominator is too large\n\n");
		return 0;
	}
	return 1;
	
}

//flush the input stream
void flushinstream(void){
	char c;
	while(((c = fgetc(stdin)) != EOF) && (c != '\n'));
}

//find the value of the numerator and denominator
void findvalues(void){
	strncpy(num, buffer1, slash_position);//numerator
	strcpy(denom, &(buffer1[slash_position+1]));//denominator
	
}

//validate the input numbers
int validatenumbers(void){
	//max and min integer values to compare the numerator and denominator to
	const int MAX_INT = 0x7FFFFFFF;
	const int MIN_INT = 0x80000000;
	
	if(strlen(num) > 12 || strlen(denom) > 12){//denominator or numerator is too large
		return 0;
	}
	
	
	long long toss = atoll(numcheck);//convert to long long
	if(toss > MAX_INT || toss < MIN_INT){//check if numerator is in range
		return 0;
	}
	
	long long toss1 = atoll(denomcheck);//convert to long long
	if(toss1 > MAX_INT || toss1 < MIN_INT || toss1 == 0){//check if denominator is in range
		return 0;
	}
	return 1;
}

//find the GCD of the fraction, this is a recursive method
int findGCD(int a, int b){
	if(b == 0){
		return a;
	}
	else{
		return findGCD(b, a%b);
	}
}
//exclusive or to check the negative status of the fraction
int is_negative(int numerator, int denominator){
	return(numerator^denominator) < 0 ? 1 : 0;
}
