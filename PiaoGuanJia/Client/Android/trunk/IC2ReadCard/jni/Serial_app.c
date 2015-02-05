#include <jni.h>
#include <stdlib.h>   
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>
#include <sys/ioctl.h>   
#include <termios.h> 
#include <sys/types.h>
#include <sys/stat.h>
#include <assert.h>
#include "Serial_app.h"
/*********************************************************/

#define max_buffer_size   1295   /*定义缓冲区最大宽度*/


unsigned char const   findcard[10]={0xAA,0xAA,0xAA,0x96,0x69,0x00,0x03,0x20,0x01,0x22};
unsigned char const selectcard[10]={0xAA,0xAA,0xAA,0x96,0x69,0x00,0x03,0x20,0x02,0x21};
unsigned char const   readcard[10]={0xAA,0xAA,0xAA,0x96,0x69,0x00,0x03,0x30,0x01,0x32};
unsigned char const   findcard_cmp[15]=  {0xAA,0xAA,0xAA,0x96,0x69,0x00,0x08,0x00,0x00,0x9F,0x00,0x00,0x00,0x00,0x97};
unsigned char const   selectcard_cmp[19]={0xAA,0xAA,0xAA,0x96,0x69,0x00,0x0C,0x00,0x00,0x90,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x9C};
unsigned char const   changebaud[10]={0xAA,0xAA,0xAA,0x96,0x69,0x00,0x03,0x60,0x02,0x61};
unsigned char const   changeresult[11]={0xAA,0xAA,0xAA,0x96,0x69,0x00,0x04,0x00,0x00,0x90,0x94};

const char simid_const[12]=
{

    0x05,0x00,0x01,0x00,0x70,0xb6,0x32,0x01,0x12,0xCF,0x0F,0x00,0xC1,0x35,0x34,0x11
};
JNIEXPORT jint JNICALL Java_com_serial_apps_Serialread(JNIEnv *env, jclass jc, jbyteArray data,  jbyteArray samid)
{ 
   char  hd[max_buffer_size],*rbuf; /*定义接收缓冲区*/
   int retv,i,ncount=0,time_r;
   struct termios opt;
   int fd;
  int setio,ret=0;
setio=open("/dev/msm_io_cm7",0);
   if (setio >0) 
   { 
	   ioctl(setio, 1, 1);
		usleep(1000000);  
		ioctl(setio, 1, 1);
		usleep(1000000);  

   }
   else
   {
	  // 	ret=-6;
	//goto END;
   }
  jbyte *olddata = (*env)->GetIntArrayElements(env,data,0);
  jbyte *samiddata = (*env)->GetIntArrayElements(env,samid,0);
  jsize  oldsize = (*env)->GetArrayLength(env,data);

memset(hd,0,max_buffer_size);
/*******************************************************************/
 fd = open("/dev/ttyMSM2",O_RDWR|O_NOCTTY);  /*读写方式打开串口*/
 printf("open /dev/ttyMSM2\n");
 if(fd == -1)  /*打开失败*/
 {
	ret=-1;
	goto END;
}
 
 
/*******************************************************************/

tcgetattr(fd,&opt);
cfmakeraw(&opt);
opt.c_cc[VTIME]    = 1;   /* inter-character timer unused */
opt.c_cc[VMIN]     = 0;   /* blocking read until 1 chars received */


/*****************************************************************/

cfsetispeed(&opt,B38400); /*波特率设置为115200bps*/
cfsetospeed(&opt,B38400); /*波特率设置为115200bps*/

/*******************************************************************/

tcsetattr(fd,TCSANOW,&opt);
tcflush(fd,TCIOFLUSH);
retv=write(fd, findcard, 10);
rbuf=hd; /*数据保存*/
if(retv!=10)
{   
 printf("write data err...\n");
 close(fd);
	ret=-1;
	goto END;
}	 

retv=read(fd,rbuf,15);   /*接收数据*/ 
if(retv<15)
{
	retv+=read(fd,rbuf,15-retv);   /*接收数据*/ 
	
}

if(retv!=15)
{
	 	
  printf("findcard read err... 0x=%d\n",retv);	
  close(fd);
	ret=-2;
	goto END;
}
retv=write(fd, selectcard, 10);
rbuf=hd; /*数据保存*/
if(retv!=10)
{   
 printf("selectcard write  err... 0x=%d\n",retv);
close(fd);
	ret=-3;
	goto END;
}	 

retv=read(fd,rbuf,19);   /*接收数据*/ 
if(retv<19)
{
	retv+=read(fd,&rbuf[retv],19-retv);   /*接收数据*/ 
	
}

if(retv!=19)
{    
  printf("selectcard read err...  0x=%d\n",retv);	
  for(i=0;i<retv;i++)
  printf("0x%02X,",rbuf[i]);
}
tcflush(fd,TCIOFLUSH);
retv=write(fd, readcard, 10);
rbuf=hd; /*数据保存*/
if(retv!=10)
{   
 printf("readcard write  err...\n");
close(fd);
	ret=-4;
	goto END;
}	 
printf("ready data...\n");
retv=0;
time_r=0;     
   while(ncount<1295)       

  {    
      

      retv=read(fd,rbuf,1295);
    
      if(retv==-1)
     {
       printf("err_read");
     }
     else if(retv==0)
     {
		time_r++; 
		if(time_r>10)
		break;
	 }
	 else
	 {  time_r=0;
		ncount+=retv;
        rbuf+=retv;
        if(ncount>=1295)
        break; 
		 
	 }
     
 }

/*******************************************************************/
close(fd);

  if(ncount==1295)
  {
  memcpy(samiddata,simid_const,16);
  memcpy(olddata,hd,1295);
	ret=1;
	goto END;
  }
  else
  {
	ret=-5;
	goto END;
  }
 END:
		(*env)-> ReleaseByteArrayElements(env, data, olddata, 0);
		(*env)-> ReleaseByteArrayElements(env, samid, samiddata, 0);
	  if(setio>0)
	  {
		ioctl(setio, 1, 0);
		usleep(10000);  
		close(setio);
	  }
   return ret;
}
//波特率修改为38400
JNIEXPORT jint JNICALL Java_com_serial_apps_SerialChangeBaudrate(JNIEnv *env, jclass jc)
{
	 int fd,retv=0;
	 char rbuf[50];
	 struct termios opt;

	/*******************************************************************/
	 fd = open("/dev/ttyMSM2",O_RDWR|O_NOCTTY);  /*读写方式打开串口*/
	 printf("open /dev/ttyMSM2\n");
	 if(fd == -1)  /*打开失败*/
	 return -1;
	 
	 
	/*******************************************************************/

	tcgetattr(fd,&opt);
	cfmakeraw(&opt);
	opt.c_cc[VTIME]    = 1;   /* inter-character timer unused */
	opt.c_cc[VMIN]     = 0;   /* blocking read until 1 chars received */


	/*****************************************************************/

	cfsetispeed(&opt,B115200); /*波特率设置为115200bps*/
	cfsetospeed(&opt,B115200); /*波特率设置为115200bps*/
	tcsetattr(fd,TCSANOW,&opt);
	tcflush(fd,TCIOFLUSH);
	retv=write(fd, changebaud, 10);
	if(retv!=10)
	{   
	 printf("write data err...\n");
	 close(fd);
	 return 0;
	}	 

	retv=read(fd,rbuf,45);   /*接收数据*/ 
	if(retv<11)
	{
		retv+=read(fd,rbuf+retv,45);   /*接收数据*/ 		
	}
	close(fd);
	if(memcmp(rbuf,changeresult,11)==0)
	{
		return 1;
	}
	return 0;

}
