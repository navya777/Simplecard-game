package sampleCardgame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
public class CardGame 
{
	public static String cardDeck[][]=new String[4][13];
	int scores=0;
	public static int total=0;
	
	public static void main(String[] args) 
		{   
			System.out.println("Welcome to HighLow Card game");
			
			System.out.println("Your score is based on number of correct guesses");
			System.out.println("Guess whether following card will have higher or lower value");
			
			System.out.println("Enter Player name");
			Scanner readin=new Scanner(System.in);
			String name=readin.next();
			System.out.println("Enter 'Y'to play or 'Q' to quit");
			char choice=readin.next().charAt(0);
			if(choice=='Y'|| choice=='y')
				{
				CardGame.deck();
				CardGame.trout(name);
				}
			else
				System.out.println("Hope to see you soon");
			
			readin.close();
			
		}
	
	public static void deck()
 		{	
 	      cardDeck[0][0]="Ace Diamond";
 		  cardDeck[0][10]="Diamond King";
 		  cardDeck[0][11]="Diamond Queen";
 		  cardDeck[0][12]="Diamond Jack";
 		  cardDeck[1][0]="Ace Spade";
 		  cardDeck[1][10]="Spade King";
 		  cardDeck[1][11]="Spade Queen";
 		  cardDeck[1][12]="Spade Jack";
 		  cardDeck[2][0]="Ace clover";
 		  cardDeck[2][10]="Clover King";
 		  cardDeck[2][11]="Clover Queen";
 		  cardDeck[2][12]="Clover Jack";
 		  cardDeck[3][0]="Ace Heart";
 		  cardDeck[3][10]="Heart King";
 		  cardDeck[3][11]="Heart Queen";
 		  cardDeck[3][12]="Heart Jack";
 		  for(int i=0;i<=cardDeck.length;i++)
 				{
 			  	for(int j=1;j<=9;j++)
 			  	{
 			  		if(i==0)
 			  		{
 						cardDeck[i][j]= "Diamond"+j;
 				//System.out.println(cardDeck[i][j]);
 			  		}
 			  		else if(i==1)
 			  		{
 					cardDeck[i][j]= "Spade"+j;
 					//System.out.println(cardDeck[i][j]);
 			  		}
 			  		else if(i==2)
 			  		{
 					cardDeck[i][j]= "Clover"+j;
 					//System.out.println(cardDeck[i][j]);
 			  		}
 			  		else if(i==3)
 			  		{
 					cardDeck[i][j]= "Heart"+j;
 					//System.out.println(cardDeck[i][j]);
 			  		}
 			  	}
 				}
 		}

	



   public static void trout(String player)
   {
	     int a;
		 int b;
		 

		 List <String[]>randomCards=Arrays.asList(CardGame.cardDeck);  //convert a string array to list to use shuffle function
		Collections.shuffle(randomCards);
		Random rand=new Random();
		 a=rand.nextInt(CardGame.cardDeck.length);
		
		 b=rand.nextInt(CardGame.cardDeck[3].length);
		String playername=player;
		 
		 System.out.println("Guess the card value next to "+ CardGame.cardDeck[a][b]);
		 String currentCard=CardGame.cardDeck[a][b];
		 System.out.println("If higher Enter 'H' else enter 'L'");
		 Scanner read=new Scanner(System.in);
	     char selection=read.next().charAt(0);
		 score(playername,currentCard,selection,a,b);
		 read.close();
   }
  
//}
   

public static void score(String name,String displayedCard,char choice,int r,int z)
   {
	   int score=0;
	
	   Random random1=new Random();
	   int w=random1.nextInt(CardGame.cardDeck.length);
	  
	   int h=random1.nextInt(CardGame.cardDeck[3].length);
	 
	   System.out.println(CardGame.cardDeck[w][h]);
	   		if(choice=='H'&&(w>r&&h>z))
	   		{
	   			System.out.println("You got it!!");
	   			++score;
	   			total=total+score;
	   			
	   			System.out.println(" score  is "+score);
	   			System.out.println(" Total  is "+total);
	   			
	   		}
	   		else if(choice=='L'&&(w<r&&h<z))
	   		{
	   			System.out.println("You got it!!");
	   			++score;
	   			total=total+score;
	   			
	   			System.out.println(" score is "+score);
	   			System.out.println(" Total  is "+total);
	   			
	   		}
	   		
	   		else
	   			{
	   			System.out.println("Better luck next time");
	   		    System.out.println(" Total  is "+total);
	   		 
	   		    }
	   
		
		 System.out.println("Press Y if you wish to continue else press Q");
		 Scanner option=new Scanner(System.in);
		 char selection=option.next().charAt(0);
		 if(selection=='Y')
			   trout(name);
		else
		{	
			System.out.println("Hope to see you soon");
		 updateScore_DB(name,total);
		 option.close();
		}
		
	}



   public static void updateScore_DB(String playerName,int gamescore)
   {
	   int flag=0;
	   
	   String playersName=playerName;
		int score=gamescore;
		 try
			{
			 Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Employeeportal?useSSL=false","root","root");
		 Statement mystatemnt = myconnection.createStatement();
			
			 String wese="Select * from Score_Board.Score_Log";
			 ResultSet res=mystatemnt.executeQuery(wese);
			// res.next();
			// System.out.println(playersName);
			 while(res.next())
		 {
			
			if(res.getString(2).equals(playersName))
			 {
				String updateQuery="update Score_Board.Score_Log set Score=Score+? where Player_Name = ? ";
				PreparedStatement pmt=myconnection.prepareStatement(updateQuery);
				pmt.setInt(1, score);
			pmt.setString(2, playersName);
				
				
				
				int q= pmt.executeUpdate();
				 flag=1;
			 }
			
		 }
			if(flag==1)
			{
				myconnection.close();
			}
			 else
			 {
				 String insertql="insert into  Score_Board.Score_Log(Player_Name,Score) values (?,?)";
				 PreparedStatement pmt3=myconnection.prepareStatement(insertql);
				 pmt3.setInt(2, score);
				 pmt3.setString(1, playersName);
				
				 System.out.println(playersName);
				 System.out.println("I am in insert");
				int r= pmt3.executeUpdate();
				myconnection.close();
			 }
			 
		
  // }    
			}
		 catch(Exception ecx)
		 {
			ecx.printStackTrace();
			
		 }
		 
		 
 }
}
   



