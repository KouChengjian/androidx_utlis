# JavaPoet
https://blog.csdn.net/l540675759/article/details/82931785

# androidx_utlis
我认为框架就是一个可以满足各种需求快速叠加，扩展性不要太差，说人话，上手快的一系列库的组合<br>
一个兴趣使然出生的大杂烩框架（都是androidx的锅）<br>
整理这些年使用的两种开发工程架构（萝卜青菜各所有爱，适合自己的就好了，希望能得到灵感）<br>
以下所有库都是在本人系列项目使用，不要问多少用户<br>

## library-core
这个架构已经永和4年了，开发过无数小项目<br>
优点：
1、没有依赖，唯一算高级的就是java泛型+类的创建。
2、经历过大大小小无数需求变化，都能兼容，扩展性还可以。
3、兼容kotlin
缺点：
1、用久了，有点烦了，编写view烦
2、不喜欢都是缺点

## library-core-dagger

## library-kernel
#### 1.0.1
viewbinding
#### 1.0.2
1、移除butterknife
2、移除widget
3、添加getLayoutInflater()
#### 1.0.3
1、移除注解依赖


## 待修复
fix:apt参数为空异常
fix:recycle重新获取接口处理默认

