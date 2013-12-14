package com.challengercity.revise;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

public class DragDropList extends JList
        implements DragSourceListener, DropTargetListener, DragGestureListener {

    static DataFlavor localObjectFlavor;

    static {
        try {
            localObjectFlavor =
                    new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
    static DataFlavor[] supportedFlavors = {localObjectFlavor};
    DragSource dragSource;
    DropTarget dropTarget;
    Object dropTargetCell;
    int draggedIndex = -1;

    public DragDropList() {
        super();
        setCellRenderer(new ReorderableListCellRenderer());
        dragSource = new DragSource();
        DragGestureRecognizer dgr =
                dragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_MOVE,
                this);
        dropTarget = new DropTarget(this, this);
    }

    // DragGestureListener
    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
//        System.out.println("dragGestureRecognized");
        // find object at this x,y
        Point clickPoint = dge.getDragOrigin();
        int index = locationToIndex(clickPoint);
        if (index == -1) {
            return;
        }
        Object target = getModel().getElementAt(index);
        Transferable trans = new RJLTransferable(target);
        draggedIndex = index;
        dragSource.startDrag(dge, Cursor.getDefaultCursor(),
                trans, this);
    }
    // DragSourceListener events

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
//        System.out.println("dragDropEnd()");
        dropTargetCell = null;
        draggedIndex = -1;
        repaint();
    }

    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
    // DropTargetListener events

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
//        System.out.println("dragEnter");
        if (dtde.getSource() != dropTarget) {
            dtde.rejectDrag();
        } else {
            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
//            System.out.println("accepted dragEnter");
        }
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // figure out which cell it's over, no drag to self    
        if (dtde.getSource() != dropTarget) {
            dtde.rejectDrag();
        }
        Point dragPoint = dtde.getLocation();
        int index = locationToIndex(dragPoint);
        if (index == -1) {
            dropTargetCell = null;
        } else {
            dropTargetCell = getModel().getElementAt(index);
        }
        repaint();
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        System.out.println("drop()!");
        if (dtde.getSource() != dropTarget) {
//            System.out.println("rejecting for bad source ("
//                    + dtde.getSource().getClass().getName() + ")");
            dtde.rejectDrop();
            return;
        }
        Point dropPoint = dtde.getLocation();
        int index = locationToIndex(dropPoint);
//        System.out.println("drop index is " + index);
        boolean dropped = false;
        try {
            if ((index == -1) || (index == draggedIndex)) {
//                System.out.println("dropped onto self");
                dtde.rejectDrop();
                return;
            }
            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
//            System.out.println("accepted");
            Object dragged =
                    dtde.getTransferable().getTransferData(localObjectFlavor);
            // move items - note that indicies for insert will 
            // change if [removed] source was before target 
//            System.out.println("drop " + draggedIndex + " to " + index);
            boolean sourceBeforeTarget = (draggedIndex < index);
//            System.out.println("source is"
//                    + (sourceBeforeTarget ? "" : " not")
//                    + " before target");
//            System.out.println("insert at "
//                    + (sourceBeforeTarget ? index - 1 : index));
            
            
            ListModel lm = this.getModel();
            LinkedList<Object> ls = new LinkedList<Object>();
            for (int i = 0; i < lm.getSize(); i++) {
                ls.add(lm.getElementAt(i));
            }
            ls.remove(draggedIndex);
            ls.add((sourceBeforeTarget ? index - 1 : index), dragged);
            
            DefaultListModel dlm = new DefaultListModel();
            for (Object o : ls) {
                dlm.addElement(o);
            }
            setModel(dlm);
            
            
            dropped = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dtde.dropComplete(dropped);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    class RJLTransferable implements Transferable {

        Object object;

        public RJLTransferable(Object o) {
            object = o;
        }

        @Override
        public Object getTransferData(DataFlavor df)
                throws UnsupportedFlavorException, IOException {
            if (isDataFlavorSupported(df)) {
                return object;
            } else {
                throw new UnsupportedFlavorException(df);
            }
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor df) {
            return (df.equals(localObjectFlavor));
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return supportedFlavors;
        }
    }

    class ReorderableListCellRenderer
            extends DefaultListCellRenderer {

        boolean isTargetCell;
        boolean isLastItem;

        public ReorderableListCellRenderer() {
            super();
        }

        @Override
        public Component getListCellRendererComponent(JList list,
                Object value,
                int index,
                boolean isSelected, boolean hasFocus) {
            isTargetCell = (value == dropTargetCell);
            isLastItem = (index == list.getModel().getSize() - 1);
            boolean showSelected = isSelected
                    & (dropTargetCell == null);
            return super.getListCellRendererComponent(list, value,
                    index, showSelected,
                    hasFocus);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isTargetCell) {
                g.setColor(Color.black);
                g.drawLine(0, 0, getSize().width, 0);
            }
        }
    }
}