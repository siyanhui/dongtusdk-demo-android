# 动图宇宙SDK接入说明

欢迎使用`动图宇宙SDK`，动图宇宙迄今为止最新、最好的封装库。

我们在这里免费提供的是带UI组件的库版本。它提供了搜索动图的基础功能。在此之上，还提供了直接实现动图消息展示功能的UI组件，可以帮助含有聊天功能的应用很方便地集成动图功能。

`动图宇宙SDK`主要包括jar包、jni库、AndroidManifest.xml，以及res目录下的一些资源文件。我们建议将它作为一个模块（module）集成到gradle项目中去。

通过`动图宇宙SDK`你可以做到：

* 获取当下最流行的动图表情；

* 根据关键词搜索动图表情；

* 获得每张动图的唯一标识和必要信息，用于封装成消息进行传输；

* 在UI中播放动图表情；


## 第一步：引入module

首先将`DongtuSDK`目录复制到项目中。

修改`settings.gradle`，加入如下代码，让gradle识别这个module：

```groovy
include ':DongtuSDK'
```

找到需要使用DongtuSDK的module（一般是应用的module，或者应用所用到的UI组件库module），修改它的`build.gradle`，在dependencies中加入：

```groovy
api project(':DongtuSDK')
```

然后，module的引入就宣告完成了。

## 第二步：获取并传入必要信息

开发者将应用与SDK进行对接时，必要的接入信息如下:

* appId - 应用的App ID
* appSecret - 应用的App Secret

如您暂未获得以上接入信息，可以[在此](http://open.biaoqingmm.com/open/register/index.html)申请

开发者需要通过`Dongtu`类使用`动图宇宙SDK`的功能。在APP启动之后，需要先调用`Dongtu.configure()`函数，进行初始化。

初始化的主要目的有两个：

1. 传入在动图宇宙官网申请到的`App ID`和`App Secret`给今后生成的`Dongtu`实例使用；

2. 传入一个`Context`进行一些数据的准备（例如查找资源ID、得到cache的路径等）。

`configure`并不涉及任何主线程中的耗时操作。为了保证它的尽早执行，我们建议在Application.onCreate()中执行此函数。

初始化的代码如下：

```java
Dongtu.configure(context, APP_ID, APP_SECRET);
```

此外，在开始使用`Dongtu`的功能之前，还需要传入用户信息：

```java
Dongtu.setUserInfo(USER_ID, USER_ID, USER_GENDER, USER_ADDRESS, USER_EMAIL, USER_PHONE, extra);
```

## 第三步：使用功能

### 1. 使用联想功能和GIF搜索模块

Dongtu对象提供了两种搜索Gif的方法。

* 在用户输入时，即时进行搜索（即联想功能）。

```java
Dongtu.setupSearchPopupFor(anchor, searchBox);
```

* 显示一个全屏弹窗进行搜索（即GIF搜索模块）。

```java
Dongtu.showDongtuPopup(activity);
```

要实现发送GIF的功能，还需要传入一个回调，当用户点击某个GIF时可以得到通知：

```java
Dongtu.setSendImageListener(new DTSendImageListener() {
    @Override
    public void onSendImage(DTImage image) {
        sendMessage(image);
    }
});
```

### 2. 展示`DTImage`

可以用`DTImageView`实现`DTImage`的展示。

```java
final DTImageView imageView;
final DTImage item;
Dongtu.loadImageInto(item.getImage(), item.getId(), imageView, item.getWidth() * 2, item.getHeight() * 2, null);
```

传入的一对`width`和`height`参数被用于指定动图的显示长宽。一般建议把动图显示得稍大一些，因此上面的例子中对长和宽都乘以了2。

`DTThumbImageView`有一个`getStatus()`函数，在加载中、加载成功和加载失败时分别会返回`LOADING`、`READY`和`FAILED`。

调用`loadImageInto`时还可以传入一个`DTOutcomeListener`。在加载成功或者失败时，它的`onSuccess`或者`onFailure`函数会被相应调用。如果主动调用`getStatus()`获取加载状态无法满足开发者的需求，也可以在这个Listener的`onSuccess`和`onFailure`函数中实现相应逻辑。

### 3. 封装和解析消息

封装消息的格式与`动图宇宙SDK`无关，只要保证将必要的信息传送到接收端，令其能够正确解析和展示动图即可。

而所谓“必要的信息”主要是`Dongtu.loadImageInto`函数需要的参数：

* image（String，由`DTImage.getImage()`获得）
* ID（String，由`DTImage.getId()`获得）
* width（int，由`DTImage.getWidth()`获得）
* height（int，由`DTImage.getHeight()`获得）
* isAnimated（int，0代表否，1代表是，由`DTImage.isAnimated()`获得，iOS需要）
