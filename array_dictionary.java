package data_structures;

import java.util.Iterator; 
import java.util.NoSuchElementException; 


public interface DictionaryI<K,V> extends Iterable<K> {
	
	

	// Adds the given key/value pair to the dictionary.  Returns 
	// false if the dictionary is full, or if the key is a duplicate. 
	// Returns true if addition succeeded. 
	public boolean add(K key, V value);

	// Deletes the key/value pair identified by the key parameter. 
	// Returns true if the key/value pair was found and removed, 
	// otherwise false. 
	public boolean delete(K key);

	// Returns the value associated with the parameter key.  Returns 
	// null if the key is not found or the dictionary is empty. 
	public V getValue(K key);

	// Returns the key associated with the parameter value.  Returns 
	// null if the value is not found in the dictionary.  If more 
	// than one key exists that matches the given value, returns the 
	// first one found. 
	public K getKey(V value);

	// Returns the number of key/value pairs currently stored 
	// in the dictionary 
	public int size();

	// Returns true if the dictionary is full 
	public boolean isFull();

	// Returns true if the dictionary is empty 
	public boolean isEmpty();

	// Returns an Iterator of the keys in the dictionary
	public Iterator<K> keys();

	// Returns an Iterator of the values in the dictionary.  The 
	// order of the values must match the order of the keys. 
	public Iterator<V> values();

}

####################################################################################################

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;




public class ArrayDictionary<K,V> implements DictionaryI<K,V>{
    private int currentSize, maxSize;
    private K[] keys;
    private long modCount;
    private int first = 0,next = 0;
    
    public class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>>{
            K key;
            V value;
            
            public DictionaryNode(){}
            
            public DictionaryNode(K key, V value){
                    this.key = key;
                    this.value = value;
            }
            
            public int compareTo(DictionaryNode<K,V> node){
                    return((Comparable<K>)key).compareTo((K)node.key);
            }
            
    }
    /**
     * ArrayDictionary constructor
     * @param size maxsize of the data structure
     */
    public ArrayDictionary(int size){
            currentSize = 0;
            maxSize = size;
            keys = (K[]) new Object[maxSize];
            modCount = 0;
    }
    
                    /**
                     * Adds the given key/value pair to the dictionary.  Returns
                     * false if the dictionary is full, or if the key is a duplicate.
                     * Returns true if addition succeeded. 
                     */	 
                    public boolean add(K key, V value){
                            if(isFull()){ return false;}
                            if(currentSize/maxSize >= 0.75)
                                    maxSize *= 1.5;
                            keys[next] = (K) new DictionaryNode<K,V>(key,value);
                            currentSize++;
                            modCount++;
                            next++;
                            return true;
                            
                    }

                    /**
                     * Deletes the key/value pair identified by the key parameter.
                     * Returns true if the key/value pair was found and removed,
                     * otherwise false.
                     */ 
                    public boolean delete(K key){
                            if(isEmpty()){return false;}
                            key = keys[currentSize-1];
                            currentSize--;
                            modCount++;
                            next--;
                            return true;
                    }

                    /**
                     * Returns the value associated with the parameter key.  Returns
                     * null if the key is not found or the dictionary is empty.
                     */
                    public V getValue(K key){
                            if(isEmpty()){return null;}
                            int point = 0;
                            for(int i = 0;i<currentSize;i++)
                                    if(((Comparable<K>)key).compareTo((K)keys[i]) ==0){
                                            point = i;
                                            V temp = (V)keys[point];
                                            return temp;
                                    }
                            return null;
                    }

                    /**
                     * Returns the key associated with the parameter value.  Returns
                     * null if the value is not found in the dictionary.  If more
                     * than one key exists that matches the given value, returns the
                     * first one found.
                     */ 
                    public K getKey(V value){
                            if(isEmpty()){return null;}
                            int point = 0;
                            for(int i = 0;i<currentSize;i++)
                                    if(((Comparable<V>)value).compareTo((V)keys[i]) ==0){
                                            point = i;
                                            K temp = (K)keys[point];
                                            return temp;
                                    }
                            return null;
                    }

                    /**
                     * Returns the number of key/value pairs currently stored
                     * in the dictionary
                     */ 
                    public int size(){
                            return currentSize;
                    }

                    /**
                     * Returns true if the dictionary is full 
                     */
                    public boolean isFull(){
                            return currentSize == maxSize;
                    }

                    /**
                     * Returns true if the dictionary is empty 
                     */
                    public boolean isEmpty(){
                            return currentSize == 0;
                    }

                    /**
                     * Returns an Iterator of the keys in the dictionary 
                     */ 
                    public Iterator<K> keys(){
                            return new KeyIterator();
                    }

                    /**
                     * Returns an Iterator of the values in the dictionary.  The
                     * order of the values must match the order of the keys.
                     */ 
                    public Iterator<V> values(){
                            return new ValueIterator();
                    }
                    /**
                     * 
                     * @author Brian
                     * KeyIterator class to be used as the key iterator
                     */
                    public class KeyIterator implements Iterator<K>{
                            private int iteratorPoint;
                            private int count;
                            private long modCounter;
                            
                            public KeyIterator(){
                                    iteratorPoint = first;
                                    count = currentSize;
                            }
                            
                            public boolean hasNext(){
                                    if(modCounter != modCount)
                                            throw new ConcurrentModificationException();
                                    return iteratorPoint<currentSize;
                            }
                            public K next(){
                                    if(!hasNext())
                                            throw new NoSuchElementException();
                                    count--;
                                    if(++iteratorPoint == maxSize)
                                            iteratorPoint = 0;
                                    return (K)keys[iteratorPoint++];
                            }
                            public void remove(){
                                    throw new UnsupportedOperationException();
                            }
                            
                    }
                    /**
                     * 
                     * @author Brian
                     * ValueIterator class to be used as the value iterator
                     */
                    public class ValueIterator implements Iterator<V>{
                            private int iteratorPoint;
                            private int count;
                            private long modCounter;
                            
                            public ValueIterator(){
                                    iteratorPoint = first;
                                    count = currentSize;
                            }
                            
                            public boolean hasNext(){
                                    if(modCounter != modCount)
                                            throw new ConcurrentModificationException();
                                    return iteratorPoint<currentSize;
                            }
                            
                            public V next(){
                                    if(!hasNext())
                                            throw new NoSuchElementException();
                                    count--;
                                    if(++iteratorPoint == maxSize)
                                            iteratorPoint = 0;
                                    return (V)keys[iteratorPoint++];
                            }
                            
                            public void remove(){
                                    throw new UnsupportedOperationException();
                            }
                    }

                    public Iterator<K> iterator() {
                            return new KeyIterator();
                    }
            
    

}
