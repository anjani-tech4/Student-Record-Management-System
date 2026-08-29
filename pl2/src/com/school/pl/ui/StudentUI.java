package com.school.pl.ui;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import com.school.pl.model.*;
import java.awt.event.*;
import javax.swing.text.*;
import com.school.pl.pojo.*;
import java.io.*;
import com.school.pl.model.exceptions.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;


public class StudentUI extends JFrame
{
private int width = 1100;
private int height= 600;
enum MODES
{
VIEW,
EDIT,
ADD,
DELETE,
EXPORT_TO_PDF
}
private JTextField nameTextField;
private JRadioButton maleRadioButton;
private JRadioButton femaleRadioButton;
private ButtonGroup genderButtonGroup;

private MODES mode;
private String iconsFolder = "C:\\java\\app1\\pl2\\testcases\\icons\\";
//private String iconsFolder="icons"+File.separator;
private JTable table;
private StudentTableModel studentTableModel;
private Container container;


//components for top search panel
private JPanel searchPanel;
private JComboBox searchInComboBox;
private JLabel searchInLabel;
private JTextField searchTextField;
private JLabel searchLabel;
private JButton searchButton; 
private Color searchTextFieldFoundStateColor;
private Color searchTextFieldNotFoundStateColor;

// components for south details panel
private JPanel detailsPanel;
private JLabel rollNumberValueLabel;
private JLabel nameValueLabel;
private JLabel genderValueLabel;
private JLabel rollNumberLabel;
private JLabel nameLabel;
private JLabel genderLabel;

//Components for south buttons Panel
private JPanel buttonsPanel;
private JButton addButton;
private JButton saveButton;
private JButton editButton;
private JButton updateButton;
private JButton deleteButton;
private JButton cancelButton;
private JButton pdfButton;



//------------------------------------------
public StudentUI()
{
super("Student Management");
this.studentTableModel=new StudentTableModel();
this.table=new JTable(this.studentTableModel);


//1.next kyaa? 
// ans:  Font size .

// 2. next panel

JScrollPane scrollPane=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
this.container=this.getContentPane();
this.container.setLayout(new BorderLayout());
this.container.add(scrollPane, BorderLayout.CENTER);

//top search panel components
searchPanel= new JPanel();
searchInComboBox=new JComboBox();
searchInComboBox.addItem("Name");
searchInComboBox.addItem("Roll number");
searchInLabel=new JLabel("Search in");
searchTextField=new JTextField(20);
searchLabel= new JLabel("Search");
searchButton = new JButton("Search", new ImageIcon(iconsFolder+"search.png"));
searchPanel.setLayout(new FlowLayout());
searchPanel.add(searchInLabel);
searchPanel.add(searchInComboBox);
searchPanel.add(searchLabel);
searchPanel.add(searchTextField);
searchPanel.add(searchButton);
container.add(searchPanel, BorderLayout.NORTH);

//south details panel
detailsPanel=new JPanel();
detailsPanel.setLayout(null); //because we are setting position  
//rollNumberLabel=new JLabel(" Roll Number: ");
rollNumberLabel = new JLabel(" Roll Number: ", new ImageIcon(iconsFolder+"roll.png"), JLabel.LEFT);
//------------------------------------add Button programming 11 July----------
nameTextField=new JTextField(50);
maleRadioButton=new JRadioButton("Male",true);
femaleRadioButton=new JRadioButton("Female",false);
genderButtonGroup=new ButtonGroup();
genderButtonGroup.add(maleRadioButton);
genderButtonGroup.add(femaleRadioButton);
rollNumberValueLabel= new JLabel("");
//-----------------------------------------------------------------------------
nameLabel=new JLabel(" Name: ", new ImageIcon(iconsFolder+"name.png"), JLabel.LEFT);
nameValueLabel= new JLabel("");

genderLabel=new JLabel(" Gender: ", new ImageIcon(iconsFolder+"gender.png"), JLabel.LEFT);
genderValueLabel= new JLabel("");

rollNumberLabel.setBounds(20,10,150,25);
rollNumberValueLabel.setBounds(175,10,100,25);

nameLabel.setBounds(20,40,150,25);
nameValueLabel.setBounds(175,40,500,25);
nameTextField.setBounds(175,40,500,25);
genderLabel.setBounds(20,70,150,25);
genderValueLabel.setBounds(175,70,150,25);

maleRadioButton.setBounds(175,70,100,25);
femaleRadioButton.setBounds(285,70,100,25);
detailsPanel.add(rollNumberLabel);
detailsPanel.add(rollNumberValueLabel);
detailsPanel.add(nameLabel);
detailsPanel.add(nameValueLabel);
detailsPanel.add(genderLabel);
detailsPanel.add(genderValueLabel);
detailsPanel.add(maleRadioButton);
detailsPanel.add(femaleRadioButton);
detailsPanel.add(nameTextField);
container.add(detailsPanel, BorderLayout.SOUTH);

// Buttons Panel Components

buttonsPanel= new JPanel();
buttonsPanel.setLayout(null);
addButton = new JButton(new ImageIcon(iconsFolder+"add.png"));
ImageIcon icon = new ImageIcon(iconsFolder+"add.png");
System.out.println("Width: " + icon.getIconWidth()); // -1 means load fail 
editButton = new JButton(new ImageIcon(iconsFolder+"edit.png"));
updateButton = new JButton(new ImageIcon(iconsFolder+"save.png"));
deleteButton = new JButton(new ImageIcon(iconsFolder+"delete.png"));
cancelButton = new JButton(new ImageIcon(iconsFolder+"cancel.png"));
pdfButton = new JButton(new ImageIcon(iconsFolder+"pdf.png"));
saveButton = new JButton(new ImageIcon(iconsFolder+"save.png"));
addButton.setBounds(10,10,40,40);
saveButton.setBounds(10,10,40,40);
editButton.setBounds(65,10,40,40);
updateButton.setBounds(65,10,40,40);
deleteButton.setBounds(120,10,40,40);
cancelButton.setBounds(175,10,40,40);
pdfButton.setBounds(230,10,40,40);
buttonsPanel.setBounds(this.width/2-280/2,105,280,60);
buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
buttonsPanel.add(addButton);
buttonsPanel.add(saveButton);
buttonsPanel.add(editButton);
buttonsPanel.add(updateButton);
buttonsPanel.add(deleteButton);
buttonsPanel.add(cancelButton);
buttonsPanel.add(pdfButton);
detailsPanel.add(buttonsPanel);

this.setComponentsAppearance();

addEventListeners();

setWindowAppearance();
setViewMode();
setVisible(true);
}
//----------------------------------------------
public void setWindowAppearance()
{
Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
setSize(width, height);
int x,y;
x=dimension.width/2-width/2;
y=dimension.height/2-height/2;
setSize(x,y);
setSize(width, height);
setLocation(x, y);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);

}

private void setComponentsAppearance()
{

// kyuki consistency rehni chahie , aur baar baar nhi likhna pade..
Font tableFont= new Font("Times New Roman", Font.PLAIN,24);
Font searchPanelFont=new Font("Times New Roman", Font.PLAIN,20);
Font detailsPanelFont = new Font("Times New Roman", Font.PLAIN,20);


this.table.setFont(tableFont);
this.table.setRowHeight(30);
this.table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD,24));
this.table.getColumnModel().getColumn(0).setPreferredWidth(100);
this.table.getColumnModel().getColumn(1).setPreferredWidth(100);
this.table.getColumnModel().getColumn(2).setPreferredWidth(400);
//this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
this.table.getTableHeader().setResizingAllowed(false);
this.table.getTableHeader().setReorderingAllowed(false);
//DefaultTableCellRenderer leftRenderer;
//leftRenderer=new DefaultTableCellRenderer();
TableCellRenderer defaultHeaderRenderer = this.table.getTableHeader().getDefaultRenderer();

TableCellRenderer nameHeaderRenderer = new TableCellRenderer(){
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = defaultHeaderRenderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        if(c instanceof JLabel)
        {
            JLabel label = (JLabel) c;
            label.setFont(new Font("Times New Roman", Font.BOLD, 24));
            label.setHorizontalAlignment(JLabel.LEFT);
        }
        return c;
    }
};

this.table.getColumnModel().getColumn(2).setHeaderRenderer(nameHeaderRenderer);
//leftRenderer.setHorizontalAlignment(JLabel.LEFT);
//leftRenderer.setFont(new Font("Times New Roman", Font.BOLD,24));
//this.table.getColumnModel().getColumn(2).setHeaderRenderer(leftRenderer);
this.table.getTableHeader().repaint();//Header ko force redraw karne ke liye, taaki upar ke changes turant screen pe reflect ho jayein.
this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Table mein user ek time pe sirf ek hi row select kar sakta hai (multiple rows nahi). 
//--------------------------------------------------
this.searchInLabel.setFont(searchPanelFont);
this.searchInComboBox.setFont(searchPanelFont);
this.searchLabel.setFont(searchPanelFont);
this.searchTextField.setFont(searchPanelFont);
this.searchButton.setFont(searchPanelFont);
this.searchInLabel.setFont(searchPanelFont);
//-------------------------------------------------------
this.rollNumberValueLabel.setFont(detailsPanelFont);
this.nameValueLabel.setFont(detailsPanelFont);
this.genderValueLabel.setFont(detailsPanelFont);
this.rollNumberLabel.setFont(detailsPanelFont);
this.nameLabel.setFont(detailsPanelFont);
this.genderLabel.setFont(detailsPanelFont);

nameTextField.setFont(detailsPanelFont);
maleRadioButton.setFont(detailsPanelFont);
femaleRadioButton.setFont(detailsPanelFont);

this.detailsPanel.setPreferredSize(new Dimension(100,200));
//----------------------------------------------------------

this.searchTextFieldFoundStateColor=this.searchTextField.getForeground();
this.searchTextFieldNotFoundStateColor=Color.red;
}
//----------------------------------------------------------
public void addEventListeners()
{
this.nameTextField.addKeyListener(new KeyAdapter(){
public void keyTyped(KeyEvent ev)
{
//fires after key is pressed and released bus has not become part of text in textfield
if(nameTextField.getText().length()==50) ev.consume();
}
});
((AbstractDocument)this.nameTextField.getDocument()).setDocumentFilter(new DocumentFilter()
{
@Override
public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs)
    throws BadLocationException
{
int currentLength = fb.getDocument().getLength();
int availableSpace = 50 - currentLength;
if(availableSpace <= 0) return;

if(text.length() > availableSpace)
{
text = text.substring(0, availableSpace);
}
super.insertString(fb, offset, text, attrs);
}

@Override
public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
    throws BadLocationException
{
int currentLength = fb.getDocument().getLength() - length;
int availableSpace = 50 - currentLength;
if(availableSpace <= 0) return;

if(text.length() > availableSpace)
{
text = text.substring(0, availableSpace);
}
super.replace(fb, offset, length, text, attrs);
}
});
this.addButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
StudentUI.this.setAddMode();
}
});

this.saveButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
String name=StudentUI.this.nameTextField.getText().trim();
String gender="Male";
if(StudentUI.this.femaleRadioButton.isSelected()) gender="Female";
if(name.length()==0)
{
JOptionPane.showMessageDialog(StudentUI.this, "Name required");
StudentUI.this.nameTextField.requestFocus();
return;
}
if(name.length()>50)
{
JOptionPane.showMessageDialog(StudentUI.this, "Name cannot exceed 50 characters");
StudentUI.this.nameTextField.requestFocus();
return;

}
Student student= new Student(0, name, gender);
try
{
int insertedAtIndex= StudentUI.this.studentTableModel.addStudent(student);
setViewMode();

StudentUI.this.studentTableModel.fireTableDataChanged();
// code to select a row based on value on insertedAtIndex
StudentUI.this.table.setRowSelectionInterval(insertedAtIndex, insertedAtIndex);
// code to get the row in drawable area
StudentUI.this.table.scrollRectToVisible(
    StudentUI.this.table.getCellRect(insertedAtIndex, 0, true));
String searchTextFieldContent=StudentUI.this.searchTextField.getText().trim();
StudentUI.this.searchTextField.setForeground(StudentUI.this.searchTextFieldFoundStateColor);
int searchInIndex=StudentUI.this.searchInComboBox.getSelectedIndex();
if(searchInIndex==0) //If searching by Name
{
if(searchTextFieldContent.length()>0) //checks that the search box is not empty.
{
if(!name.toUpperCase().startsWith(searchTextFieldContent.toUpperCase()))
{
StudentUI.this.searchTextField.setText("");
}
}
}else if(searchInIndex==1)
{
StudentUI.this.searchTextField.setText("");
}
}
catch(ModelException modelException)
{
JOptionPane.showMessageDialog(StudentUI.this, modelException.getMessage());
}
}

});
this.editButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
StudentUI.this.setEditMode();
}
});
this.deleteButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
StudentUI.this.setDeleteMode();
}
});

this.pdfButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
StudentUI.this.setExportToPDFMode();
}
});

this.cancelButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
StudentUI.this.setViewMode();
}

});
// table selection changed event handler
this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
public void valueChanged(ListSelectionEvent ev)
{
int index=table.getSelectedRow();
 if(index==-1) return;
Student student = StudentUI.this.studentTableModel.getStudentByIndex(index);
rollNumberValueLabel.setText(String.valueOf(student.getRollNumber()));
nameValueLabel.setText(String.valueOf(student.getName()));
genderValueLabel.setText(String.valueOf(student.getGender()));

}
});


//----------------------------------------4 July'26------------------------


//SearchInComboBox event handler
searchInComboBox.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
StudentUI.this.searchTextField.setForeground(StudentUI.this.searchTextFieldFoundStateColor);
StudentUI.this.searchTextField.setText("");
}
});


//Search TextField Change event handler

this.searchTextField.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
StudentUI.this.applySearch();
}
});

this.searchTextField.addKeyListener(new KeyAdapter(){
public void keyTyped(KeyEvent ev)
{
if(StudentUI.this.searchInComboBox.getSelectedIndex()==0) return;
if(ev.getKeyChar()<48 || ev.getKeyChar()>57)
{
ev.consume();
}
} //keyTyped ends
public void keyPressed(KeyEvent ev)
    {
        if(ev.isControlDown() && ev.getKeyCode()==KeyEvent.VK_V)
        {
            ev.consume();
        }
    }
});

/*
Block only Ctrl + V → keyPressed() with VK_V.
Block all pasted alphabets → DocumentFilter.
*/
((AbstractDocument)this.searchTextField.getDocument()).setDocumentFilter(new DocumentFilter()
{
    @Override
    public void replace(FilterBypass fb, int offset, int length,
                    String text, AttributeSet attrs)
        throws BadLocationException
{
    if(StudentUI.this.searchInComboBox.getSelectedIndex()==1)
    {
        if(text.matches("[0-9]*"))
        {
            super.replace(fb, offset, length, text, attrs);
        }
    }
    else
    {
        super.replace(fb, offset, length, text, attrs);
    }
}
});

//-------------------------------4 July'26 ends----------------------------

this.searchTextField.getDocument().addDocumentListener(new DocumentListener(){
public void searchTextChanged()
{
if(StudentUI.this.searchInComboBox.getSelectedIndex()==0) applySearch();
}
public void changedUpdate(DocumentEvent ev)
{
searchTextChanged();
}
public void insertUpdate(DocumentEvent ev)
{
searchTextChanged();
}
public void removeUpdate(DocumentEvent ev)
{
searchTextChanged();
}
});

//search Button event handler
this.searchButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
applySearch();
}
});
} //add listeners part ends
private void applySearch()
{
String searchWhat=StudentUI.this.searchTextField.getText().trim();
if(searchWhat.length()==0) return;
if(searchInComboBox.getSelectedIndex()==0) //search name
{
String namePart=searchWhat;
int rowIndex=StudentUI.this.studentTableModel.searchStudentByPartialName(namePart);
if(rowIndex==-1)
{
StudentUI.this.searchTextField.setForeground(StudentUI.this.searchTextFieldNotFoundStateColor);
}
else
{
StudentUI.this.table.setRowSelectionInterval(rowIndex, rowIndex);
StudentUI.this.table.scrollRectToVisible(table.getCellRect(rowIndex, 0, true));
StudentUI.this.searchTextField.setForeground(StudentUI.this.searchTextFieldFoundStateColor);
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}else if(StudentUI.this.searchInComboBox.getSelectedIndex()==1) //search RollNumber
{
int rollNumber=Integer.parseInt(searchWhat);
int rowIndex=StudentUI.this.studentTableModel.searchStudentByRollNumber(rollNumber);
if(rowIndex==-1)
{
StudentUI.this.searchTextField.setForeground(StudentUI.this.searchTextFieldNotFoundStateColor);
} //if(rowIndex==-1) ends
else
{
StudentUI.this.table.setRowSelectionInterval(rowIndex, rowIndex);
StudentUI.this.table.scrollRectToVisible(table.getCellRect(rowIndex, 0, true));
StudentUI.this.searchTextField.setForeground(StudentUI.this.searchTextFieldFoundStateColor);

} //else ends
}
}
private void setViewMode()
{
this.mode=MODES.VIEW;
this.addButton.setVisible(true);
this.addButton.setEnabled(true);
this.saveButton.setVisible(false);
this.editButton.setVisible(true);
this.editButton.setEnabled(true);
this.updateButton.setVisible(false);
this.deleteButton.setVisible(true);
this.deleteButton.setEnabled(true);
this.cancelButton.setVisible(true); // not really  required, as it will never be hidden
this.cancelButton.setEnabled(false);
this.pdfButton.setVisible(true);
this.pdfButton.setEnabled(true);
this.searchInComboBox.setEnabled(true);
this.searchTextField.setEnabled(true);
this.table.setEnabled(true);

//rollNumberValueLabel

this.rollNumberValueLabel.setVisible(true);
this.nameTextField.setText("");
this.nameTextField.setVisible(false);
this.maleRadioButton.setSelected(true);
this.maleRadioButton.setVisible(false);
this.femaleRadioButton.setVisible(false);
this.nameValueLabel.setVisible(true);
this.genderValueLabel.setVisible(true);
this.searchButton.setEnabled(true);


}
private void setAddMode()
{
this.mode=MODES.ADD;
this.addButton.setVisible(false);
this.saveButton.setVisible(true);
this.editButton.setVisible(true);
this.editButton.setEnabled(false);
this.updateButton.setVisible(false);
this.deleteButton.setVisible(true);
this.deleteButton.setEnabled(false);
this.cancelButton.setVisible(true); // not really  required, as it will never be hidden
this.cancelButton.setEnabled(true);
this.pdfButton.setVisible(true);
this.pdfButton.setEnabled(false);
this.searchInComboBox.setEnabled(false);
this.searchTextField.setEnabled(false);
this.table.setEnabled(false);
this.searchButton.setEnabled(false);

this.rollNumberValueLabel.setVisible(false);
this.nameValueLabel.setVisible(false);
this.genderValueLabel.setVisible(false);

this.nameTextField.setText("");
this.nameTextField.setVisible(true);
this.maleRadioButton.setSelected(true);
this.maleRadioButton.setVisible(true);
this.femaleRadioButton.setVisible(true);




}
private void setEditMode()
{
this.mode=MODES.EDIT;
this.addButton.setVisible(true);
this.addButton.setEnabled(false);
this.saveButton.setVisible(false);
this.editButton.setVisible(false);
this.editButton.setEnabled(false);
this.updateButton.setVisible(true);
this.deleteButton.setVisible(true);
this.deleteButton.setEnabled(false);
this.cancelButton.setVisible(true); // not really  required, as it will never be hidden
this.cancelButton.setEnabled(true);
this.pdfButton.setVisible(true);
this.pdfButton.setEnabled(false);
this.searchInComboBox.setEnabled(false);
this.searchTextField.setEnabled(false);
this.table.setEnabled(false);
this.searchButton.setEnabled(false);


}

private void setDeleteMode()
{
this.mode=MODES.DELETE;
this.addButton.setVisible(true);
this.addButton.setEnabled(false);
this.saveButton.setVisible(false);
this.editButton.setVisible(true);
this.editButton.setEnabled(false);
this.updateButton.setVisible(false);
this.deleteButton.setVisible(true);
this.deleteButton.setEnabled(false);
this.cancelButton.setVisible(true); // not really  required, as it will never be hidden
this.cancelButton.setEnabled(true);
this.pdfButton.setVisible(true);
this.pdfButton.setEnabled(false);
this.searchInComboBox.setEnabled(false);
this.searchTextField.setEnabled(false);
this.table.setEnabled(false);
this.searchButton.setEnabled(false);


}
private void setExportToPDFMode()
{
this.mode=MODES.EXPORT_TO_PDF;
this.addButton.setVisible(true);
this.addButton.setEnabled(false);
this.saveButton.setVisible(false);
this.editButton.setVisible(true);
this.editButton.setEnabled(false);
this.updateButton.setVisible(false);
this.deleteButton.setVisible(true);
this.deleteButton.setEnabled(false);
this.cancelButton.setVisible(true); // not really  required, as it will never be hidden
this.cancelButton.setEnabled(false);
this.pdfButton.setVisible(true);
this.pdfButton.setEnabled(false);
this.searchInComboBox.setEnabled(false);
this.searchTextField.setEnabled(false);
this.table.setEnabled(false);
this.searchButton.setEnabled(false);

exportToPDF();
setViewMode();
}

private File choosePDFFile()
{
JFileChooser fileChooser;
int option;
File file;

fileChooser=new JFileChooser();

fileChooser.setDialogTitle("Export Student Report");

option=fileChooser.showSaveDialog(this);

if(option!=JFileChooser.APPROVE_OPTION) return null;

file=fileChooser.getSelectedFile();

if(file.getName().toLowerCase().endsWith(".pdf")==false)
{
file=new File(file.getAbsolutePath()+".pdf");
}

return file;
}
private Document createPDF(File file) throws Exception
{
Document document;
PdfWriter pdfWriter;

document=new Document();

pdfWriter=PdfWriter.getInstance(document,
new FileOutputStream(file));

document.open();

return document;
}
private void addHeading(Document document) throws Exception
{
com.itextpdf.text.Font headingFont;
Paragraph heading;

headingFont=new com.itextpdf.text.Font(
com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
22,
com.itextpdf.text.Font.BOLD);

heading=new Paragraph(
"Student Management System",
headingFont);

heading.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);

document.add(heading);
// Gap after heading
document.add(new Paragraph(" "));
}
private void addLogo(Document document) throws Exception
{

}
private void addNewPage(Document document)
{
document.newPage();
}

private PdfPTable createTable()
{
PdfPTable table;

table=new PdfPTable(3);

table.addCell("Roll Number");
table.addCell("Name");
table.addCell("Gender");

return table;
}

private void fillTable(PdfPTable table)
{
Student student;

for(int i=0;i<this.studentTableModel.getRowCount();i++)
{
student=this.studentTableModel.getStudentByIndex(i);

table.addCell(
String.valueOf(student.getRollNumber()));

table.addCell(student.getName());

table.addCell(student.getGender());
}
}

private void addTable(Document document,
PdfPTable table) throws Exception
{
document.add(table);
}
private void closePDF(Document document)
{
document.close();
}

private void showSuccessMessage()
{
JOptionPane.showMessageDialog(
this,
"PDF Exported Successfully");
}
private void exportToPDF()
{
Document document;
PdfPTable table;
File file;

try
{
file=choosePDFFile();

if(file==null) return;

document=createPDF(file);

addHeading(document);

addLogo(document);

table=createTable();

fillTable(table);

addTable(document,table);

closePDF(document);
Desktop.getDesktop().open(file);

showSuccessMessage();

}catch(Exception exception)
{
JOptionPane.showMessageDialog(
this,
exception.getMessage());
}
}

}


