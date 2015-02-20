package io.github.emanual.app.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * DataPool is tool that helps you easier to do some operation in the
 * SharedPreference <br>
 * <h1>Like : add(),remove(),get(),set()</h1> <h1>Note :</h1> <li>In currrent
 * version of android SDK,DataPool is not support multi thread
 * 
 * @author Jayin Ton
 * 
 */
public class DataPool {
	/**
	 * 保存全部会员中的缓存数据键，以后面加1 到20 表明是第几个，
	 * SP_KEY_ALLMEMBER+i 才是真正的键
	 */
	public final static String SP_KEY_ALLMEMBER ="SP_KEY_ALLMEMBER";
	/**
	 * 我的好友
	 */
	public final static String SP_KEY_FRIEND_MEMBER ="SP_KEY_FRIEND_MEMBER";
	
	/**
	 * 全站动态
	 */
	public final static String SP_KEY_ALL_DUNAMIC ="SP_KEY_ALL_DUNAMIC";
	
	/**
	 * 好友动态
	 */
	public final static String SP_KEY_FRIEND_DUNAMIC ="SP_KEY_FRIEND_DUNAMIC";
	
	
    public final static String SP_Name_User = "User"; //SharedPreference's name
    public final static String SP_Key_User = "user";//key name in SharedPreference 
    public final static String SP_Key_User_Name = "username"; //登录账号
    public final static String SP_Key_User_PSW ="psw";   //登录密码
	private Context context;
	private String DataPoolName = "DataPool";
	private SharedPreferences sp;

	public DataPool(Context context) {
		this("DataPool", context);
	}

	public DataPool(String dataPoolName, Context context) {
		this.DataPoolName = dataPoolName;
		this.context = context;
		sp = context.getSharedPreferences(this.DataPoolName,
				Context.MODE_PRIVATE);
	}
	
	/**
	 * add a key(String)-value(Serializable object) into SharedPreference
	 * 
	 * @param key
	 *            key of this pair
	 * @param value
	 *            value of this pair
	 * @return true if add successfully
	 */
	public boolean put(String key, Serializable value) {
		boolean flag = false;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(value);
			String base64String = Base64.encodeToString(bos.toByteArray(),
					Base64.DEFAULT);
			sp.edit().putString(key, base64String).apply();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * add a key(String,default is "temp")-value(object) into SharedPreference
	 * 
	 * @param value
	 *            value of this pair ,with the defalut key="temp"
	 * @return true if add successfully
	 */
	public boolean put(Serializable value) {
		return put("temp", value);
	}

	/**
	 * get value(Serializable Object) from DataPool(SharedPreference) with the
	 * given Key
	 * 
	 * @param key
	 *            key of this pair ,with the defalut key="temp"
	 * @return one Serializable Object
	 */
	public Serializable get(String key) {
		if (!contains(key))
			return null;
		String base64String = sp.getString(key, "");
		byte[] buf = Base64.decode(base64String, Base64.DEFAULT);
		ByteArrayInputStream bis = new ByteArrayInputStream(buf);
		ObjectInputStream ois = null;
		Serializable result = null;
		try {
			ois = new ObjectInputStream(bis);
			result = (Serializable) ois.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return result;
	}

	/**
	 * check if DataPool(SharedPreference) contain the given key
	 * 
	 * @param key
	 *            the given key
	 * @return true if it contains
	 */
	public boolean contains(String key) {
		return sp.contains(key);
	}

	/**
	 * check if DataPool(SharedPreference) is empty
	 * 
	 * @return true if it's empty
	 */
	public boolean isEmpty() {
		return sp.getAll().size() == 0;
	}

	/**
	 * remove a key-value of this pair
	 * 
	 * @param key
	 *            the key of this pair
	 * @return true if it removes successfully
	 */
	public void remove(String key) {
		if (!contains(key))
			return;
		sp.edit().remove(key).apply();
	}

	/**
	 * remove all the key-value of this pair
	 * 
	 * @return
	 */
	public void removeAll() {
		if (isEmpty())
			return;
		Map<String, ?> map = sp.getAll();
		for (String key : map.keySet()) {
			remove(key);
		}
	}

	/**
	 * updata the key-value
	 * 
	 * @param key
	 *            key of this pair
	 * @param value
	 *            value of this pair
	 * @return true if set successfully
	 */
	public boolean set(String key, Serializable value) {
		if (!contains(key))
			return false;
		return put(key, value);
	}

}
