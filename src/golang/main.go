package main

import (
    "net/http"
    "fmt"
)

func main() {
    err := http.ListenAndServeTLS(":8080", "key.pem", "cert.pem", nil)
    if err != nil {
        fmt.Println(err)
        return
    }
}
