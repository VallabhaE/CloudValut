package main

import (
	"github.com/gin-gonic/gin"
)

func main() {
	var router *gin.Engine = gin.Default()
	__routes(router)
	router.Run(":8080")

}

func __routes(router *gin.Engine) {
	router.POST("/login", gin.WrapF(LoginHandler))
	router.POST("/signup", gin.WrapF(SignupHandler))
}
