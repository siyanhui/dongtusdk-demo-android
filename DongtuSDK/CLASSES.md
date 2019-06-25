# 类说明

## 管理类

### Dongtu

* 版本信息

  ```java
  public static final String VERSION
  ```

* 传入APP_ID和APP_SECRET进行初始化

  ```java
  public static void configure(Context context, String appId, String appSecret)
  ```

* 传入用户信息，包括唯一标识`userId`、昵称`userName`、性别`gender`、住址`address`、电子邮箱`email`和电话`phone`这几个参数。任意一项不存在或未知时可以传`null`。

  ```java
  public static void setUserInfo(String userId, String userName, DTGender gender, String address, String email, String phone, JSONObject extra)
  ```

* 为一个`DTEditText`配置联想功能。用户在editText中输入文字时，搜索结果弹窗会实时显示在anchor上方。`DTEditText`继承自`android.widget.EditText`，和后者基本没有区别。

  ```java
  public static void setupSearchPopupFor(View anchor, DTEditText editText)
  ```

* 显示Gif搜索模块

  ```java
  public static void showDongtuPopup(Activity activity)
  ```
  
* 传入发送Gif回调

  ```java
  public static void setSendImageListener(DTSendImageListener listener)
  ```

* 加载`DTImageView`

  * `path`实际上是一个Dongtu内部可以解析的字符串，用于获取图片文件
  * `id`是图片的ID
  * `view`为需要加载图片的View
  * `width`与`height`是图片的展示尺寸，单位为像素
  * `listener`在加载成功或失败时会得到通知

  ```java
  public void loadImageInto(String path, String id, DTImageView view, int width, int height, DTOutcomeListener listener)
  ```

* 自定义“动图详情”页面，即点击`DTImageView`后打开的Activity的样式。

  * 如果只需要更改一部分样式，可以扩展`DTDefaultImageDetailConfigProvider`这个类，只重写需要修改返回值的函数

  ```java
  public static void setImageDetailConfigProvider(DTImageDetailConfigProvider provider)
  ```

## UI定制类

### DTImageDetailConfigProvider

目前可供定制的是标题栏的样式。

* 标题栏背景色

  ```java
  int backgroundColor()
  ```

* 标题栏文字颜色

  ```java
  int titleTextColor()
  ```

* 标题栏高度，单位为DP

  ```java
  int heightDP()
  ```

* 标题栏文字大小，单位为SP

  ```java
  int titleTextSizeSP()
  ```

### DTImageViewConfigProvider

目前可供自定义的是“小尾巴”部分的样式

* “小尾巴”背景色

  ```java
  int tipBackground();
  ```

* “小尾巴”文字颜色1（左半部分）

  ```java
  int tipText1();
  ```

* “小尾巴”文字颜色2（右半部分）

  ```java
  int tipText2();
  ```

## UI组件

### DTImageView

* 图片加载是否成功

  * 返回的status一共有三种，`LOADING`、`READY`和`FAILED`，分别代表加载中、成功和失败 

  ```java
  public Status getStatus()
  ```

* 自定义View样式

  * 如果只需要更改一部分样式，可以扩展`DTDefaultImageViewConfigProvider`这个类，只重写需要修改返回值的函数

  ```java
  public void config(DTImageViewConfigProvider provider)
  ```

### DTEditText

继承自`android.widget.EditText`，功能上没有任何变化