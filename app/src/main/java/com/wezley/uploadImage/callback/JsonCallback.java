package com.wezley.uploadImage.callback;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;
import com.wezley.uploadImage.Exception.ServerException;
import com.wezley.uploadImage.model.ServerResponse;
import com.wezley.uploadImage.model.SimpleResponse;
import com.wezley.uploadImage.util.Convert;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：针对返回类型不是ServerResponse 异常处理
 * -
 * -
 * -
 * -
 * -我的注释都已经写的不能再多了,不要再来问我怎么获取数据对象,怎么解析集合数据了,你只要会 gson ,就会解析
 * -
 * -如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
 * -如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
 * -如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
 * -
 * -
 * -
 * ================================================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
/*        request.headers("header1", "HeaderValue1")//
                .params("params1", "ParamsValue1")//
                .params("token", "3215sdf13ad1f65asd4f3ads1f");*/
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     * <pre>
     * OkGo.get(Urls.URL_METHOD)//
     *     .tag(this)//
     *     .execute(new DialogCallback<ServerResponse<ServerModel>>(this) {
     *          @Override
     *          public void onSuccess(ServerResponse<ServerModel> responseData, Call call, ServerResponse response) {
     *              handleResponse(responseData.data, call, response);
     *          }
     *     });
     * </pre>
     */
    @Override
    public T convertSuccess(okhttp3.Response response) throws Exception {

        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
        //以下代码是通过泛型解析实际参数,泛型必须传
        //这里为了方便理解，假如请求的代码按照上述注释文档中的请求来写，那么下面分别得到是

        //com.lzy.demo.callback.DialogCallback<com.lzy.demo.model.ServerResponse<com.lzy.demo.model.ServerModel>> 得到类的泛型，包括了泛型参数
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        //我们的示例代码中，只有一个泛型，所以取出第一个，得到如下结果
        //com.lzy.demo.model.ServerResponse<com.lzy.demo.model.ServerModel>
        Type type = params[0];
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        // 这里这么写的原因是，我们需要保证上面我解析到的type泛型，仍然还具有一层参数化的泛型，也就是两层泛型
        // 如果你不喜欢这么写，不喜欢传递两层泛型，那么以下两行代码不用写，并且javabean按照
        // https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md 这里的第一种方式定义就可以实现
        if (!(type instanceof ParameterizedType))
        {
           throw new IllegalStateException("没有填写泛型参数");//没有填写泛型参数*/
        }
        //如果确实还有泛型，那么我们需要取出真实的泛型，得到如下结果
        //class com.lzy.demo.model.ServerResponse
        //此时，rawType的类型实际上是 class，但 Class 实现了 Type 接口，所以我们用 Type 接收没有问题
        Type rawType = ((ParameterizedType) type).getRawType();
        //这里获取最终内部泛型的类型 com.lzy.demo.model.ServerModel
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用

        if (typeArgument == Void.class) {
            //无数据类型,表示没有data数据的情况（以  new DialogCallback<ServerResponse<Void>>(this)  以这种形式传递的泛型)
            SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
            response.close();
            //noinspection unchecked
            return (T) simpleResponse.toServerResponse();
        } else if (rawType == ServerResponse.class) {
            //有数据类型，表示有data
            ServerResponse serverResponse = Convert.fromJson(jsonReader, type);
            Log.i("JsonCallback","msg="+serverResponse.message +" status="+serverResponse.state+"type="+type.toString());
            response.close();
            int code = serverResponse.code;
            Log.i("JsonCallback","serverResponse.code="+Integer.toString(code));
            if (code == 200) {
                //noinspection unchecked
                return (T) serverResponse;
            } else if(!TextUtils.isEmpty(serverResponse.message)){
                throw new ServerException(code,serverResponse.message);
            } else {
                throw new IllegalStateException("未知错误");
            }
        } else {
            response.close();
            throw new IllegalStateException( "基类错误无法解析!");
        }
    }

}