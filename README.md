目前主流的leanback风格的Android tv应用

# 效果图
<img src="https://raw.githubusercontent.com/zhangtiansheng/TVLeanBackApp/master/image/TV_2.png" width="600" height = "500"/>
<img src="https://raw.githubusercontent.com/zhangtiansheng/TVLeanBackApp/master/image/TV_3.png"/>
<img src="https://raw.githubusercontent.com/zhangtiansheng/TVLeanBackApp/master/image/TV_1.png"/>

数据以块为单位组织

                             Block
                               |
             __________________|___________________
             |    ...  |           |       ...    |
           Block      Block    Display Item     Display Item
           
           

#  展示块

UI 组织和展示块一一对应</br>
```
  block首页
    block(推荐)    block(影视大全)    block(游戏应用)   block(个人中心)   block(设置)
    block轮播图
       电视剧
       电影
       ...
       
    block 电影
        电影入口
        电影单元
        ...
```

#  展示单元定义 DisplayItem
展示数据和业务无关，展示数据能展示，视频，游戏，应用，广告</br>
满足以下条件：</br>
1. 展示文本，包括title, sub_title, description, hint(left, center, right), 角标，</br>
2. 展示图片，包括图标，背景，动画，海报</br>
3. 点击跳转，数据可以得到最终跳转的Intent，URI，包括为跳转准备应用下载，安装</br>
4. 商业化打点，包括公司的Post打点和第三方的GET打点</br>



