package com.qianxia.cardfight;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.view.View.*;
import android.view.*;
import android.opengl.*;
import android.app.*;
import android.content.*;

public class GameActivity extends BaseActivity implements View.OnClickListener
{
    private int life = 20, weapon = 0, place = 0, enemySum = 0, mark=0, s = 0;
	private int[] card = new int[44], house = new int[4];
	private ProgressBar lifeBar;boolean skip = false;
	private Button restart;
	private TextView lifeValue, weaponValue;
	private ImageView left, right, weaponCard;
	private ImageView[] houseCard = new ImageView[4];
	private Random random = new Random();
	private AlertDialog.Builder dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		lifeBar = (ProgressBar)findViewById(R.id.lifebar);
		lifeValue = (TextView)findViewById(R.id.lifevalue);
		weaponValue = (TextView)findViewById(R.id.weaponvalue);
		houseCard[0] = (ImageView)findViewById(R.id.card1);houseCard[0].setOnClickListener(this);
		houseCard[1] = (ImageView)findViewById(R.id.card2);houseCard[1].setOnClickListener(this);
		houseCard[2] = (ImageView)findViewById(R.id.card3);houseCard[2].setOnClickListener(this);
		houseCard[3] = (ImageView)findViewById(R.id.card4);houseCard[3].setOnClickListener(this);
		left = (ImageView)findViewById(R.id.left);left.setOnClickListener(this);
		right = (ImageView)findViewById(R.id.right);right.setOnClickListener(this);
		weaponCard = (ImageView)findViewById(R.id.weaponcard);
		restart = (Button)findViewById(R.id.restart);restart.setOnClickListener(this);
		dialog = new AlertDialog.Builder(GameActivity.this);
		
		life=20;lifeBar.setProgress(life);
		randomCard();
		for(int i = 0;i < 4;i++){house[i]=0;}
		setHouse();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.card1:clickCard(0);break;
			case R.id.card2:clickCard(1);break;
			case R.id.card3:clickCard(2);break;
			case R.id.card4:clickCard(3);break;
			case R.id.left:
				if(skip){
					dialog.setTitle("跳");
					dialog.setMessage("不能连续跳两个房间");
					dialog.setCancelable(true);
					dialog.setPositiveButton("返回", new DialogInterface.
					    OnClickListener() {
							public void onClick(DialogInterface dialog, int which){}});
					dialog.show();
				}
				else{
					skip = true;s=0;
					for(int i = 0;i<4;i++){
						if(house[i]!=0){s++;}
					}
					for(int i = place;i<44;i++){card[i-s]=card[i];}
					for(int i = 44-s;i<44;i++){
						for(int j = 0;j<4;j++){
							if(house[j]!=0){card[i]=house[j];house[j]=0;break;}
						}
					}
					place -= s;setHouse();
				}
				break;
			case R.id.right:s = 0;
				for(int i=0; i<4; i++){if(house[i] != 0){s++;}}
				if(s>1){
					dialog.setTitle("进");
					dialog.setMessage("剩余卡牌过多，不能进入下一房间");
					dialog.setCancelable(false);
					dialog.setPositiveButton("返回", new DialogInterface.
					    OnClickListener() {
							public void onClick(DialogInterface dialog, int which){}});
					dialog.show();break;
				}
				if(skip){skip = false;}
				setHouse();
				break;
			case R.id.restart:
				dialog.setTitle("重新开始");
				dialog.setMessage("你确定重新开始？");
				dialog.setCancelable(true);
				dialog.setPositiveButton("确定", new DialogInterface.
					OnClickListener() {
						public void onClick(DialogInterface dialog, int which){
							recreate();
						}});
				dialog.setNegativeButton("取消", new DialogInterface.
					OnClickListener() {
						public void onClick(DialogInterface dialog, int which){}});
				dialog.show();
				break;
		}
		if(enemySum == 208){
			mark = life;
			for(int i =0;i<4;i++){
				if(house[i]/100==1){mark += house[i]%100;}
			}
			for(int i=place;i<44;i++){
				if(card[i]/100==1){mark += card[i]%100;}
			}
			dialog.setTitle("Victory");
			dialog.setMessage("分数：" + mark);
			dialog.setCancelable(false);
			dialog.setPositiveButton("确定", new DialogInterface.
				OnClickListener() {
					public void onClick(DialogInterface dialog, int which){
						recreate();
					}});
			dialog.show();
		}
	}
	
	private void clickCard(int i){
		if(house[i] == 0){return;}
		if(house[i]/100 == 1){
			life += house[i]%100;
			if(life > 20){life = 20;}
			lifeBar.setProgress(life);
			house[i] = 0;lifeValue.setText(Integer.toString(life));
			houseCard[i].setVisibility(View.INVISIBLE);
		}
		else if(house[i]/100 == 2){
			weapon = house[i]%100;setCardImage(weaponCard, house[i]);
			house[i]=0;weaponValue.setText(Integer.toString(weapon));
			houseCard[i].setVisibility(View.INVISIBLE);
		}
		else{
			houseCard[i].setVisibility(View.INVISIBLE);
			enemySum += house[i]%100;
			if(house[i]%100<=weapon){
				weapon = house[i]%100;
				weaponValue.setText(Integer.toString(weapon));
				house[i] = 0;
			}
			else{
				life -= house[i]%100 - weapon;
				if(life <= 0){
					house[i] = 0;life = 0;
				    lifeBar.setProgress(life);
					lifeValue.setText(Integer.toString(life));
					dialog.setTitle("Failure");
					dialog.setCancelable(false);
					dialog.setMessage("分数：" + (enemySum-208));
					dialog.setPositiveButton("重新开始", new DialogInterface.
						OnClickListener() {
							public void onClick(DialogInterface dialog, int which){
								recreate();
							}});
					dialog.show();
				}
				house[i] = 0;
				lifeBar.setProgress(life);
				lifeValue.setText(Integer.toString(life));
			}
		}
	}
	
	private void randomCard(){
		int j = 0;s = 0;
		for(int i = 1; i <= 4; i++){
			for(j = 2;(i < 3&&j <= 10)||(i > 2&&j <= 14); j++){
				card[s] = i*100+j;
				s++;
			}
		}
		for(int i = 0, a, b;i < 44;i++){
			a = card[i];b = random.nextInt(43);
			card[i] = card[b];card[b] = a;
		}
	}
	
	private void setHouse(){
		for(int i = 0; i < 4;i++){
			if(house[i] != 0){continue;}
			if(place == 44){
				for(;i < 4;i++){
					house[i]=0;houseCard[i].setVisibility(View.INVISIBLE);
				}
				break;
			}
			house[i] = card[place];houseCard[i].setVisibility(View.VISIBLE);
			setCardImage(houseCard[i], house[i]);place ++;
		}
	}
	
	private void setCardImage(ImageView v,int id){
		switch(id){
			case 102:v.setImageResource(R.drawable.c102);break;
			case 103:v.setImageResource(R.drawable.c103);break;
			case 104:v.setImageResource(R.drawable.c104);break;
			case 105:v.setImageResource(R.drawable.c105);break;
			case 106:v.setImageResource(R.drawable.c106);break;
			case 107:v.setImageResource(R.drawable.c107);break;
			case 108:v.setImageResource(R.drawable.c108);break;
			case 109:v.setImageResource(R.drawable.c109);break;
			case 110:v.setImageResource(R.drawable.c110);break;
			case 202:v.setImageResource(R.drawable.c202);break;
			case 203:v.setImageResource(R.drawable.c203);break;
			case 204:v.setImageResource(R.drawable.c204);break;
			case 205:v.setImageResource(R.drawable.c205);break;
			case 206:v.setImageResource(R.drawable.c206);break;
			case 207:v.setImageResource(R.drawable.c207);break;
			case 208:v.setImageResource(R.drawable.c208);break;
			case 209:v.setImageResource(R.drawable.c209);break;
			case 210:v.setImageResource(R.drawable.c210);break;
			case 302:v.setImageResource(R.drawable.c302);break;
			case 303:v.setImageResource(R.drawable.c303);break;
			case 304:v.setImageResource(R.drawable.c304);break;
			case 305:v.setImageResource(R.drawable.c305);break;
			case 306:v.setImageResource(R.drawable.c306);break;
			case 307:v.setImageResource(R.drawable.c307);break;
			case 308:v.setImageResource(R.drawable.c308);break;
			case 309:v.setImageResource(R.drawable.c309);break;
			case 310:v.setImageResource(R.drawable.c310);break;
			case 311:v.setImageResource(R.drawable.c311);break;
			case 312:v.setImageResource(R.drawable.c312);break;
			case 313:v.setImageResource(R.drawable.c313);break;
			case 314:v.setImageResource(R.drawable.c314);break;
			case 402:v.setImageResource(R.drawable.c402);break;
			case 403:v.setImageResource(R.drawable.c403);break;
			case 404:v.setImageResource(R.drawable.c404);break;
			case 405:v.setImageResource(R.drawable.c405);break;
			case 406:v.setImageResource(R.drawable.c406);break;
			case 407:v.setImageResource(R.drawable.c407);break;
			case 408:v.setImageResource(R.drawable.c408);break;
			case 409:v.setImageResource(R.drawable.c409);break;
			case 410:v.setImageResource(R.drawable.c410);break;
			case 411:v.setImageResource(R.drawable.c411);break;
			case 412:v.setImageResource(R.drawable.c412);break;
			case 413:v.setImageResource(R.drawable.c413);break;
			case 414:v.setImageResource(R.drawable.c414);break;
		}
	}
	
}
