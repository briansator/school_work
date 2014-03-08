#include <iostream>;

class LinkedList{
	 //template <class T>
public:
	LinkedList() : head(NULL), tail(NULL){};
	~LinketList();
	bool insertFront(listNode **head,int data);
	bool insertLast(listNode **tail, int data);
	bool deleteNode(listNode **head, listNode **toDelete);
	bool deleteHead(listNode **head);
	bool deleteTail(listNode **head, listNode **tail);
	void deleteList(listNode **head);
	listNode<Integer> find( ListNode<Integer> head, int data);

protected:
	//this is our Node class, with appropriate method declarations
	 class listNode{
	 	public:
	 		listNode( const T &val) : next (NULL), data (val){};
	 		~listNode() {};
	 		listNode *findNext() const {return next;};
	 		const T& value() const {return data;};
	 		void setNext (listNode *node) {next = node;};
	 		void setValue (const T &value) {data = value;};

	 	private:
	 		listNode *next;
	 		T        data;

	 };

	 listNode *head;
	 listNode *tail;
};
//insert a node to the front of the linkedlist
 bool LinkedList :: insertFront ( listNode **head, int data){
 	listNode *newNode = new listNode();
 	if(!newNode) return false;

 	newNode->data = data;
 	newNode->next = *head;
 	*head = newNode;
 	return true;
 }
//inster a node to the back of the linkedlist
 bool LinkedList :: insertLast ( listNode **tail, int data){
 	listNode *newNode = new listNode();
 	if(!newNode) return false;

 	newNode->data = data;
 	*tail->next = newNode;
 	*tail = newNode;
 	return true;
 }
//delete a given node from the linked list
 bool LinkedList :: deleteNode (listNode **head, listNode *toDelete){
 	listNode *elem;

 	if(!head || !*head || !toDelete){//checking for null pointers
 		return false;
 	}

 	elem = *head;
 	if(toDelete == *head){//check if delete node is the head
 		*head = elem->next;
 		delete(toDelete);
 		return true;
 	}

 	while(elem){
 		if(elem->next == toDelete){//note checking to find the node the occurs prior to the node to be deleted
 			elem->next = toDelete->next;
 			delete(toDelete);
 			return true;//toDelete was found
 		}
 		elem = elem->next;
 	}
 	return false; //toDelete was not found
 }
//delete the whole list
 void LinkedList :: deleteList (listNode **head){//method to delete the entire list
 	listNode *toDelete = *head;

 	while(toDelete){
 		listNode *next = toDelete->next;
 		delete(toDelete);
 		toDelete = next;
 	}
 	*head = NULL;
 }
//delete the head of the linkedlist and update the new head
 bool LinkedList :: deleteHead(listNode **head){
 	if(!head || !*head){//check to make sure it is not an empty list
 		return false;
 	}
 	listNode *elem;
 	elem = *head->next;
 	delete(head);
 	*head = elem;
 	return true;
 }
//delete the tail of the linked list and update the new tail
 bool LinkedList :: deleteTail(listNode **head, listNode **tail){
 	if(*head.next() == NULL){
 		delete(head);
 		*head = NULL;
 		*tail = NULL;
 	}

 	listNode *elem = *head;
 	while(elem != null && elem.next() != *tail){
 		elem = elem.next();
 	}

 	elem.next() = NULL;
 	delete(tail);
 	*tail = elem;
 }
//find a the element in the linkedlist node with the given data
listNode<Integer> LinkedList :: find( ListNode<Integer> head, int data){//caller must check for null return value
 	ListNode<Integer> elem = head;
 	while(elem != null && elem.value() != data){
 		elem = elem.next();
 	};
 	return elem;
 };

 main(){
 	cout << "Main function fired";
 };
