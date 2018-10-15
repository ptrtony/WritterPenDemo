// IBackService.aidl
package org.feng.sockettest.server;

// Declare any non-default types here with import statements

interface IBackService {
    boolean sendMessage(String message);
    	void close();
}
