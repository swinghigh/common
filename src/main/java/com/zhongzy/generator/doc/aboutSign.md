# 签名验签
#一.渠道端签名
渠道端，请求聚合平台，需要对请求数据签名。

1.将接口实体内容组成JSON字符串
{"secretKey":"code","data":{"tripType":"OW","airlineType":1},"timestamp":"2023-06-30 10:10:47.278"}

2.将上述JSON字符串（此时不含sign）并进行MD5,得到md5串:
9a258dc2fc7ac1ce2dba7ea55691af97

3.对md5串用渠道端自己的私钥进行签名（采用RSA SHA256withRSA）,得到签名sign。 注：渠道端的公钥已保存在数据库渠道表里（比如通过页面回填）


#二.组装请求报文
1.将sign设置到请求报文里面，得到最终请求报文
{"secretKey":"code","data":{"tripType":"OW","airlineType":1},"sign":"WSMml3yAcg3M95ebTDZA4t3s3aFrTnhRy79NAWdBNHYW5UfhueLoVEDxb/s/D8vI6iaKNyi0wmlho3L0hL3Ey++vjCIEFXlKN7kn2m/r+5P01PhK67CPwaSXh7rJ8KcIqq8Gp8WbmRvVrb0XhuOiKm/osDov4pOjU2r8AZf8SVE=","timestamp":"2023-06-30 10:10:47.278"}

#三.接收应答报文
{"msg":"成功","code":"0000","data":[{"destinationCode":"HAK","tripType":"OW","airlineType":1,"departureCityName":"重庆1","departureCode":"CKG","destinationCityName":"海口"}],"success":true,"sign":"CzVoqaA+32vZs6O2gq0vBkexSu/6wte9JWS9HkU0NnKXv5gNyiWuRx+Mc7n0AcY+iKWtF4IMmpEG07dLfhNoWxv3+bX4+qyjpR49wg63N0HUDwcCWp3W825TqSXsm8oZOhgN7Z0WjCk9lHYbsac1Qm/CJC6zshHBZSgKH6/URBY="}

code，msg必返

#四.验证签名
渠道端需要对聚合平台返回的数据进行验签

1.将接收的应答报文转为JSONObject，并移除sign字段，得到新的JSONObject
{"msg":"成功","code":"0000","data":[{"destinationCode":"HAK","tripType":"OW","airlineType":1,"departureCityName":"重庆1","departureCode":"CKG","destinationCityName":"海口"}],"success":true}

2.对没有sign的JSONObject，根据响应数据类型，构造出参json
{"code":"0000","data":[{"airlineType":1,"departureCityName":"重庆1","departureCode":"CKG","destinationCityName":"海口","destinationCode":"HAK","tripType":"OW"}],"msg":"成功","success":true}

3.对出参json进行md5
e77b5252b29234e2c53b4f74e4db7db4

4.验证签名：对md5用平台设置的渠道公钥（平台为每个渠道都设置了一套公私钥）计算签名并和返回的签名值sign比较
验签通过，说明是平台返回的可信任数据



#五.服务端验签
聚合平台网关，需要对渠道端过来的请求数据验签

1.得到请求数据，并转为JSONObject
{"secretKey":"code","data":{"tripType":"OW","airlineType":1},"sign":"WSMml3yAcg3M95ebTDZA4t3s3aFrTnhRy79NAWdBNHYW5UfhueLoVEDxb/s/D8vI6iaKNyi0wmlho3L0hL3Ey++vjCIEFXlKN7kn2m/r+5P01PhK67CPwaSXh7rJ8KcIqq8Gp8WbmRvVrb0XhuOiKm/osDov4pOjU2r8AZf8SVE=","timestamp":"2023-06-30 10:10:47.278"}

2.将上述JSONObject移除sign，并将新的JSONObject转为json字符串
{"secretKey":"code","data":{"tripType":"OW","airlineType":1},"timestamp":"2023-06-30 10:10:47.278"}

3.对上述json字符串，进行md5，得到md5串:
9a258dc2fc7ac1ce2dba7ea55691af97

4.验证签名：对md5串用渠道端提供的公钥进行签名和sign比较。 注：渠道端的公钥已保存在数据库渠道表里（比如通过页面回填）
验签通过，继续进行业务处理，验签不通过，返回202验签失败。