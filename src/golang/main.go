package main

import (
    "net/http"
    "fmt"
    "io/ioutil"
    "encoding/json"
)

type Users struct {
    Users string `json:"message"`
}

var storage = []Users{}

func main() {
    http.HandleFunc("/login", login)

    err := http.ListenAndServeTLS(":8080", "../src/golang/crt/messenger.jobjot.co.nz.crt", "../src/golang/key/messenger.jobjot.co.nz.key", nil)
    if err != nil {
        fmt.Println(err)
        return
    }
}

func login(w http.ResponseWriter, r *http.Request) {
    SetGeneralHeaders(w)
    fmt.Println("Message Request Made")

    user := Users{}

    //Reading request body.
    defer r.Body.Close()
    body, err := ioutil.ReadAll(r.Body)
    if err != nil {
        fmt.Println(err)
        return
    }

    //Unmarshalling the JSON object and storing it in the User struct.
    err = json.Unmarshal(body, &user)
    if err != nil {
        fmt.Println(err)
        return
    }

    storage = append(storage, user)
    fmt.Println(storage)

    w.Write([]byte(`[{"message": "Message has been recorded."}]`))

    return
}

func SetGeneralHeaders(w http.ResponseWriter) {
    w.Header().Set("Cache-Control", "no-store")
    w.Header().Set("Access-Control-Allow-Origin", "*")
    w.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS")
    w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Accept, Origin")
}
