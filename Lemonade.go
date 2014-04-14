package main

import (
    "github.com/conformal/gotk3/gtk"
    "log"
)

func main() {
    gtk.Init(nil)

    win, err := gtk.WindowNew(gtk.WINDOW_TOPLEVEL)
    if err != nil {
        log.Fatal("Unable to create window:", err)
    }
    win.SetTitle("Lemonade - web-scanner")
    win.Connect("destroy", func() {
        gtk.MainQuit()
    })

    grid, err := gtk.GridNew()
    if err != nil {
        log.Fatal("Unable to create grid:", err)
    }
    grid.SetOrientation(gtk.ORIENTATION_VERTICAL)


    entry, err := gtk.EntryNew()
    if err != nil {
        log.Fatal("Unable to create entry:", err)
    }

    spnBtn, err := gtk.SpinButtonNewWithRange(0.0, 1.0, 0.001)
    if err != nil {
        log.Fatal("Unable to create spin button:", err)
    }

    nb, err := gtk.NotebookNew()
    if err != nil {
        log.Fatal("Unable to create notebook:", err)
    }
grid.Add(entry)
//    grid.Add(btn)
    grid.Add(lab)
    
    grid.Add(spnBtn)

    grid.Attach(nb, 1, 1, 1, 2)
    nb.SetHExpand(true)
    nb.SetVExpand(true)

    // Add a child widget and tab label to the notebook so it renders.
   /* nbChild, err := gtk.LabelNew("Notebook content")
    if err != nil {
        log.Fatal("Unable to create button:", err)
    }
    nbTab, err := gtk.LabelNew("Tab label")
    if err != nil {
        log.Fatal("Unable to create label:", err)
    }*/
  //  nb.AppendPage(nbChild, nbTab)

    // Add the grid to the window, and show all widgets.
    win.Add(grid)
    win.ShowAll()

    gtk.Main()
} 