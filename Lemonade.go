package main

import (
    "errors"
    "github.com/conformal/gotk3/gtk"
    "github.com/opesun/goquery"
    "log"
    "fmt"
    "net/http"
    //"io"
    "io/ioutil"
)


// Setup the Window.
func setupWindow() *gtk.Window {
    w, _ := gtk.WindowNew(gtk.WINDOW_TOPLEVEL)
    w.Connect("destroy", gtk.MainQuit)
    w.SetDefaultSize(500, 300)
    w.SetPosition(gtk.WIN_POS_CENTER)
    w.SetTitle("Lemonade - web-scanner")

    return w
}

func parsePage(html string) {
    x, err := goquery.ParseString(html)
    if err != nil {
        log.Fatal("Parse error:", err)
        return
    }

    links := x.Find("a")
    for _, link := range links {
        attrs := link.Node.Attr
        for _, attr := range attrs {
            if attr.Key == "href" {
                fmt.Print(attr.Val, "\n")
            }
        }
    }
}

func main() {

    gtk.Init(nil)

    win := setupWindow()

    topGrid, err := gtk.GridNew()
    if err != nil {
        log.Fatal("Unable to create grid:", err)
    }
    topGrid.SetOrientation(gtk.ORIENTATION_HORIZONTAL)

    urlLabel, err := gtk.LabelNew(" URL: ")
    if err != nil {
        log.Fatal("Unable to create label:", err)
    }

    urlEntry, err := gtk.EntryNew()
    if err != nil {
        log.Fatal("Unable to create entry:", err)
    }

    startButton, err := gtk.ButtonNewWithLabel("Start")
    if err != nil {
        log.Fatal("Unable to create entry:", err)
    }

    startButton.Connect("clicked", func() {
        client := &http.Client{
            CheckRedirect: func(r *http.Request, via []*http.Request) error {
                return errors.New("redirect")
            },
        }
        url, _ := urlEntry.GetText();
        resp, err := client.Get(url)
        if err != nil {
            log.Fatal("HTTP request error:",err)
        }

        body, err := ioutil.ReadAll(resp.Body)
        if err != nil {
            log.Fatal(err)
        }
        parsePage(string(body))
        return
    })

    topGrid.Add(urlLabel)
    topGrid.Add(urlEntry)
    topGrid.Add(startButton)
    win.Add(topGrid)

    win.ShowAll()

    gtk.Main()
}