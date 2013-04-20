*----------------------------------------------------------------------
*----------------------------------------------------------------------
* Programmer: Brian Sator
* Class Account: masc0456
* Assignment or Title: Homework Assignment #3
* Filename: prog3.s
* Date completed: 11/03/11  
*----------------------------------------------------------------------
* Problem statement:Convert User submitted base 10 Number to any number base 2 - 16
* Input: unsigned 16 bit base 10 number & base to convert to
* Output: number in the specified base
* Error conditions tested: 
* Included files: 
* Method and/or pseudocode: 
* References: 
*----------------------------------------------------------------------
*
        ORG     $0
        DC.L    $3000           * Stack pointer value after a reset
        DC.L    start           * Program counter value after a reset
        ORG     $3000           * Start at location 3000 Hex
*
*----------------------------------------------------------------------
*
#minclude /home/ma/cs237/bsvc/iomacs.s
#minclude /home/ma/cs237/bsvc/evtmacs.s
*
*----------------------------------------------------------------------
*
* Register use
*
*----------------------------------------------------------------------
*
start:  	initIO                  * Initialize (required for I/O)
		setEVT			* Error handling routines
	*	initF			* For floating point macros only	
		lineout	title
		* Recieve Number Input and Store
		lineout	fprompt
input1:		linein	fbuffer
		move.w	D0,D6
		tst.w	D6		*ensure user entered a value
		BEQ	error1
		subq.w	#1,D6
		lea	fbuffer,A0	*make sure number has valid digits
next:		move.b	(A0)+,D1	*0-9
		cmpi.b	#$30,D1		*character greater than or equal to 0		
		BLT	error1
next1:		cmpi.b	#$39,D1		*character less than or eqaul to 9
		BGT	error1
		dbra	D6,next		*this part checks to ensure the number
next2:		cvta2	fbuffer,D0	*is in the valid range
		move.l	D0,D3
		cmpi.w	#0,D0		*input is greater than or equal to 0
		BLT	error2
		cmpi.l	#65535,D0	*input is less than or equal to 65535
		BGT	error2
		BLE	prompt2	
		* Recieve Base Input and Store
prompt2:	lineout	sprompt
input2:		linein	sbuffer
		move.w	D0,D5
		tst.w	D5		*ensure user entered a value
		BEQ	error1a
		subq.w	#1,D5
		lea	sbuffer,A1  	*make sure number has valid digits 
nexta:		move.b	(A1)+,D2	*0-9  
		cmpi.b	#$30,D2		*character greater than or equal 0 
		BLT	error1a
next1a:		cmpi.b	#$39,D1		*character less than or equal to 9
		BGT	error1a
		dbra	D5,nexta	*this part checks to ensure the number
next2a:		lea	number,A5
		clr	D6
		cvta2 	sbuffer,D0	*is in the valid range
		move.l	D0,D4
		cmpi.w	#2,D0		*input is greater than or equal to 2
		BLT	error3
		cmpi.w	#16,D0		*input is less than or equal to 16
		BGT	error3
		BLE	loopa
carryinput:	move.w	D1,D4
		move.l	D5,D3
loopa:		divu	D4,D3
		move.w	D3,D1
		swap	D3
		move.w	D3,D5
		cmpi.w	#10,D5
		BGT	rmgreater
rmless:		addi.w	#30,D3		*remainder less than 10
		move.b	D3,(A5)+
		addi.b	#1,D6
		move.l	D6,D2
		cmpi.w	#0,D1
		BNE	carryinput
		BEQ	flip
rmgreater:	addi.w	#37,D3		*remainder greater than 10
		move.b	D3,(A5)+
		addi.b	#1,D6
		move.l	D6,D2
		cmpi.w	#0,D1
		BNE	carryinput
flip:		subq.w	#1,D6
		move.b	#number,(A6)+
		cmpi.w	#0,D6
		BNE	flip
		move.b	#0,(A3)+
		BRA	printans
		
		
		                                                                                                
		
	
error1:		lineout err
		BRA	input1
error1a:	lineout err2
		BRA	input2	
error2:		lineout	err1
error3:		lineout	err2
		BRA 	input2
		
printans:	lineout	answer
       		break                   * Terminate execution
*
*----------------------------------------------------------------------
*       Storage declarations
title:		dc.b	'Program #3, Brian Sator, masc0456',0
fprompt:	dc.b	'Enter a base 10 number',0
sprompt:	dc.b	'Enter the base to convert to:',0
fbuffer:	ds.b	80
sbuffer:	ds.b	80
answer:		dc.b	'The answer is: '
number:		ds.b	20
err:		dc.b	'Please Submit a Valid Number',0
err1:		dc.b	'Please input a number between 0 and 65535',0
err2:		dc.b	'Please input a number between 2 and 16',0	
        end
