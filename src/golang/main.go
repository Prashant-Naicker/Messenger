package main

import (
    "net/http"
    "net"
    "fmt"
    "io/ioutil"
    "encoding/json"
    "encoding/binary"
)

type Users struct {
    Users string `json:"userName"`
}

var storage = []Users{}

func main() {
    fmt.Println("Server is running...")
    http.HandleFunc("/login", login)

    err := http.ListenAndServeTLS(":8080", "../src/golang/crt/messenger.jobjot.co.nz.crt", "../src/golang/key/messenger.jobjot.co.nz.key", nil)
    if err != nil {
        fmt.Println(err)
        return
    }
}

func SetGeneralHeaders(w http.ResponseWriter) {
    w.Header().Set("Cache-Control", "no-store")
    w.Header().Set("Access-Control-Allow-Origin", "*")
    w.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS")
    w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Accept, Origin")
}

func login(w http.ResponseWriter, r *http.Request) {
    SetGeneralHeaders(w)
    fmt.Println("Request Made")

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

    if (user == Users{}) {
        w.Write([]byte(`[{"statusCode" : 2}, {"message": "Please enter a username."}]`))
        return
    }

    if (isUserOnline(user, storage)) {
        w.Write([]byte(`[{"statusCode" : 1}, {"message": "That username is already taken."}]`))
        return
    }

    storage = append(storage, user)
    fmt.Println(storage)

    w.Write([]byte(`[{"statusCode" : 200}, {"message" : "OK"}]`))
    return
}

func handleConnection(conn net.Conn) {
    fmt.Println("Connected!")

    for {
        sizeBytes, err := awaitData(conn, 2)
        if (err != nil) { fmt.Println("Disconnected"); return }

        size := binary.LittleEndian.Uint16(sizeBytes)
        data, err := awaitData(conn, int(size))
        if (err != nil) { fmt.Println("Disconnected"); return }

        fmt.Printf("> %v\n", string(data))
    }
}

func awaitData(conn net.Conn, totalSize int) ([]byte, error) {
    buffer := make([]byte, totalSize)
    readSize := 0

    for (readSize < totalSize) {
        length, err := conn.Read(buffer[readSize:]) //Read method stores bytes being read into the buffer and returns the length of bytes read.
        if err != nil { return nil, err }

        readSize += length
    }

    return buffer, nil
}

func isUserOnline(a Users, list []Users) bool {
    for _, b := range list {
        if b == a {
            return true
        }
    }
    return false
}
