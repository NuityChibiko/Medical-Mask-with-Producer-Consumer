
package producer.consumer;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumerProducerMask {

   public static void main(String[] args) throws InterruptedException  {
    System.out.println("Started working on the Medical Mask system.");
    Buffer c = new Buffer(6); // buffer has size 6
    Producer t1 = new Producer(c);
    Consumer t2 = new Consumer(c);
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    }
}

class Buffer {
private final int MaxBuffSize;
private String[] store;
private int BufferStart, BufferEnd, BufferSize;
public Buffer(int size) {
MaxBuffSize = size;
BufferEnd = -1;
BufferStart = 0;
store = new String[MaxBuffSize];
}
    public synchronized void insert(String data) {
        while (BufferSize == MaxBuffSize){
    try {
   wait();
    }
catch (InterruptedException e) {
        Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
  BufferEnd = (BufferEnd + 1) % MaxBuffSize;
  store[BufferEnd] = data;
  BufferSize++;
  notifyAll();
  }
    public synchronized String useData() {
        while (BufferSize == 0) {
     try {
        wait();
         }
  catch (InterruptedException e) {
    Thread.currentThread().interrupt();
   }
        }
 String data = store[BufferStart];
  BufferStart = (BufferStart + 1) % MaxBuffSize;
  BufferSize--;
  notifyAll();
  return data;
    }
}
   class Consumer extends Thread{
private final Buffer buffer;
    public Consumer(Buffer b) {
    buffer = b;
    }
@Override
public void run() {
try {
            sleep(2000); // ให้ Thread หลับไปก่อนครั้งแรกเพื่อให้ producer ได้ไปสร้างข้อมูลไว้ก่อน
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
while(true){
for(int i=1;i<7;i++){
String c = buffer.useData();
System.out.println("\tSlot"+i+":"+"receive "+c);
try {
      int targetNumber = (int)(Math.random()* 10); // การดึงข้อมูลอาจจะใช้เวลาไม่เท่ากันเพื่อความสมจริงจะใช้เป็น math.random()
            Thread.sleep(targetNumber*1000);  // Thread หลับไปตามเวลาที่ math.random() สุ่มออกมา
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);}
       }
     }
   }
 }
class Producer extends Thread {
private final Buffer buffer;
public Producer(Buffer b) {
buffer = b;
}
@Override
    public void run() {
   while(true){
    for(int i =1;i<7;i++){
    buffer.insert("Medical Mask");
    System.out.println("Slot"+i+":"+" produce Medical Mask");
    try{   
        Thread.sleep(4000); // Thread หลับไป 4 วิ
    }
    catch(InterruptedException ex){
         Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex); }
    
      }
    }
  }
}
    

