package main

import (
    "net/http"
    "net"
    "fmt"
    "io/ioutil"
    "encoding/json"
    "encoding/binary"
)

type User struct {
    Username string `json:"userName"`
}

type Connection struct {
    Conn net.Conn
}

var connectionList = []Connection{}
var userList = []User{}

func main() {
    fmt.Println("Server is running...")
    http.HandleFunc("/login", login)

    go listenHttpTLS()

    listenTCP()
}

func listenHttpTLS() {
    err := http.ListenAndServeTLS(":8080", "../src/golang/crt/messenger.jobjot.co.nz.crt", "../src/golang/key/messenger.jobjot.co.nz.key", nil)
    if err != nil {
        fmt.Println(err)
        return
    }
}

func listenTCP() {
    l, err := net.Listen("tcp", ":8081")
    if err != nil { fmt.Printf("%v", err); return }

    for {
        conn, err := l.Accept()
        fmt.Println(conn.RemoteAddr())
        if err != nil { fmt.Printf("%v", err); continue }

        go handleConnection(conn)
    }
}

func handleConnection(conn net.Conn) {
    fmt.Println("Connected!")

    connection := Connection{}
    connection.Conn = conn

    if (!isConnected(connection, connectionList)) { connectionList = append(connectionList, connection); }

    fmt.Println(connectionList)

    for {
        sizeBytes, err := awaitData(conn, 2)
        if (err != nil) {
            connectionList = removeConn(connection, connectionList)
            fmt.Println("Disconnected")
            fmt.Println(connectionList)
            return
        }

        size := binary.LittleEndian.Uint16(sizeBytes)
        data, err := awaitData(conn, int(size))
        if (err != nil) {
            connectionList = removeConn(connection, connectionList)
            fmt.Println("Disconnected")
            fmt.Println(connectionList)
            return
        }

        //Loop through all connections and send the received message to them.
        for i := 0; i < len(connectionList); i++ {
            sendData(connectionList[i].Conn, data)
            fmt.Println(connectionList[i])
        }

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
    fmt.Println(readSize)
    return buffer, nil
}

func sendData(conn net.Conn, message []byte) error {
    var dataLength = len(message)
    var dataLengthRaw = []byte {0, 0}

    dataLengthRaw[0] = byte(dataLength << 8 >> 8)
    dataLengthRaw[1] = byte(dataLength >> 8)

    _, err := conn.Write(dataLengthRaw)
    _, err = conn.Write(message)

    return err
}

func isUserOnline(a User, list []User) bool {
    for _, b := range list {
        if b == a {
            return true
        }
    }
    return false
}

func isConnected(a Connection, list []Connection) bool {
    for _, b := range list {
        if b == a {
            return true
        }
    }
    return false
}

func removeConn(a Connection, list []Connection) []Connection {
    for i := 0; i < len(list); i++ {
        if list[i] == a {
            list[i] = list[len(list)-1]
            list = list[:len(list)-1]
        }
    }
    return list
}



func login(w http.ResponseWriter, r *http.Request) {
    fmt.Println("Request Made - login")

    user := User{}

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

    if (user == User{}) {
        w.Write([]byte(`[{"statusCode" : 2}, {"message": "Please enter a username."}]`))
        return
    }

    if (isUserOnline(user, userList)) {
        w.Write([]byte(`[{"statusCode" : 1}, {"message": "That username is already taken."}]`))
        return
    }

    userList = append(userList, user)
    fmt.Println(userList)

    w.Write([]byte(`[{"statusCode" : 200}, {"message" : "OK"}, {"userName" : ` + user.Username + `}]`))
    return
}
