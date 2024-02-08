package com.example.universe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MypageTop extends AppCompatActivity implements View.OnClickListener{

    private RoomFacilitiesDatabase dbInstance;
    private GestureDetector gestureDetector;
    private List<RoomEntity> list;
    private List<InformationEntity> infolist;
    private List<GroupEntity> grouplist;
    private List<PostsEntity> postslist;
    private List<RepliesEntity> replieslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_top);

        ((ImageButton)findViewById(R.id.mapIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.searchIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.snsIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.infoIcon)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.mypageIcon)).setOnClickListener(this);

        ((LinearLayout)findViewById(R.id.select2)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.select3)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.select4)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.select5)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.select6)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.select9)).setOnClickListener(this);

        dbInstance = Room.databaseBuilder(getApplicationContext(), RoomFacilitiesDatabase.class, "nishikan")
                .fallbackToDestructiveMigration()
                .build();

        gestureDetector = new GestureDetector(this, new MypageTop.SwipeGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            // 左から右へのスワイプ
            if (Math.abs(diffX) > Math.abs(diffY) && diffX > 0) {
                Intent intent = new Intent(MypageTop.this, Information.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }

            return false;
        }
    }

    private void initData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            deleteRoomTable();
            deleteInfoTable();
            deleteGroupTable();
            deletePostsTable();
            deleteRepliesTable();

            // 確認後、データをロード(データベースの競合を阻止)
            loadroomdb();
            loadinfodb();
            loadgroupdb();
            loadpostsdb();
            loadrepliesdb();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getUsername().isEmpty()) {
            showUsernameDialog();
        }
    }

    private void saveUsername(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private String getUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    private void showUsernameDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("ユーザー名の登録");
        builder.setMessage("ユーザー名を入力してください (10文字以内)");

        // 入力フィールドの設定
        final EditText input = new EditText(this);
        builder.setView(input);

        // ポジティブボタン
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = input.getText().toString();
                if (!username.trim().isEmpty() && username.length() <= 10) {
                    saveUsername(username);
                    Toast.makeText(MypageTop.this, "ユーザー名「" + getUsername() + "」を保存しました", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MypageTop.this, "ユーザー名は10文字以内で入力してください", Toast.LENGTH_SHORT).show();
                    showUsernameDialog();  // 入力が無効な場合は、ダイアログを再表示
                }
            }
        });

        // ネガティブボタン
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteAndReenterUsername() {
        deleteUsername();
        Toast.makeText(MypageTop.this, "ユーザー名を削除しました", Toast.LENGTH_SHORT).show();
        showUsernameDialog();
    }

    private void deleteUsername() {
        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // "username" というキーで保存されたデータを削除
        editor.remove("username");

        // 変更を確定
        editor.apply();
    }

    private void loadroomdb(){
        RoomDAO dao = dbInstance.roomdao();
        list = dao.getall();

        if(list.size() == 0){ //テーブルの初期データ
            RoomEntity entity1 = new RoomEntity("W101(西館1F)","63席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w101image2");
            RoomEntity entity2 = new RoomEntity("W102(西館1F)","80席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,勉強,自習,落ち着いている,課題,充電できる,エアコン","w102image2");
            RoomEntity entity3 = new RoomEntity("W103(西館1F)","144席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w103image2");
            RoomEntity entity4 = new RoomEntity("W211(西館2F)","42席","8:00~23:00","一部あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,勉強,自習,落ち着いている,課題,充電できる,エアコン","w211image2");
            RoomEntity entity5 = new RoomEntity("W212(西館2F)","60席","8:00~23:00","一部あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w212image2");
            RoomEntity entity6 = new RoomEntity("W213(西館2F)","144席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w213image2");
            RoomEntity entity7 = new RoomEntity("学生ラウンジ(西館2F)","10席","8:00~23:00","一部あり","学生たちがリラックスしたり，勉強したり，友人と交流したりするための共用スペースです．","休憩スペース,わいわい,自習,談笑,待ち合わせ,充電できる,自動販売機","");
            RoomEntity entity8 = new RoomEntity("ベンディングコーナー(西館2F)","10席","8:00~23:00","一部あり","学生たちがリラックスしたり，友人と交流したりするための共用スペースです．","わいわい,休憩スペース,自動販売機,談笑,だらだら,待ち合わせ,交流","");
            RoomEntity entity9 = new RoomEntity("アクティブラーニングラボ(西館3F)","80席","8:00~23:00","あり","プログラミングやＣＧ・アニメーション映像制作などの演習で利用します．レイアウトを自由に組み替え，用途によって様々な使い方ができます．また，全席に電源コンセントと高速有線LANを完備しています．通常の講義スタイルはもちろん，実験を行う際などにはグループワーク形式にすることができます．","授業,わいわい,自習,広い,勉強,充電できる,エアコン","active2");
            RoomEntity entity10 = new RoomEntity("共通実験室(西館3F)","90席","8:00~23:00","あり","特講発表会や卒研の中間発表会などで利用します．","発表,落ち着いている,交流,雑談,わいわい,充電できる,エアコン","common2");
            RoomEntity entity11 = new RoomEntity("小金井図書館(南館1F)","298席","9:00~21:00(平日)","あり","和・洋図書約12万5千冊，和・洋雑誌・新聞約2,900点を収蔵しています．小金井市およびその周辺地域の方（在住，在勤，在学している18歳以上（高校生は除く））にもご利用いただけます．","本,落ち着いている,自習,広い,静か,充電できる,エアコン","tosyokan2");
            RoomEntity entity12 = new RoomEntity("ラーニングコモンズ(南館1F)","20席","9:00~21:00(平日)","一部あり","法政大学小金井図書館「ラーニングコモンズ」は，「学生の学習のためのスペース」です．\n学生のグループ学習等をサポートすることを目的とし，情報環境の提供によって，インターネット資源と図書館資料を活用し，知識と知恵のコラボレーションで，学習を深めるための場を提供する施設です．\n利用対象者は，学部学生，大学院生，通信教育部学生，科目等履修生．教員は，学生と共にグループで利用する場合のみ可能です．\nここでは，図書館資料やノートパソコンを持ち込んで，利用形態に合わせて机と椅子をレイアウトして，グループでホワイドボードに書きながら共通理解を深めたりパソコン画面を大画面モニターに映しながら調べた資料の確認をしたり100インチプロジェクターにパソコン画面を投影しながらプレゼンの練習をしたり学生が自由に設備を利用して，グループワークを効率的におこなえるスペースです．\nまた，室内では，無線ＬＡＮやオンデマンドプリンターが利用できます．\nペットボトルなど蓋付きの飲料のみ持ち込み可能です．","狭い,わいわい,自習,グループワーク,交流,発表,勉強","learning2");
            RoomEntity entity13 = new RoomEntity("キャリアセンター(管理棟2F)","-席","9:00~17:00(平日)","なし","進路や就職に関する一人一人のさまざまな疑問・悩みごとについて，キャリアセンタースタッフが相談に応じています．\n所属キャンパスに関係なく，全キャンパスの個別相談を利用できます．\n個別相談は基本予約制になっております．","就活,就職,インターン,進路相談,静か,企業,情報","career2");
            RoomEntity entity14 = new RoomEntity("食堂(管理棟3F)","100席","11:00~16:00(平日)","なし","学生さんの健康に気を配った安心安全のメニューです．四季おりおりのメニューや特価フェアメニューを用意しています．","わいわい,広い,食堂,ラーメン,カウンター,お昼ご飯,談笑","syokudoukanri2");
            RoomEntity entity15 = new RoomEntity("購買(管理棟3F)","-席","9:30~19:00(平日)","なし","お弁当，飲み物，パン，お菓子などがあります．\n電子マネー（スイカ，パスモ等）が使用できます．","お昼ごはん,お弁当,購買,売店,食堂,休憩スペース,電子マネー","koubai2");
            RoomEntity entity16 = new RoomEntity("購買書籍部(東館B1)","-席","10:30~16:30(平日)","なし","教科書や専門書のほか，パソコン・文具などの勉学品や日用品を取り揃えています．\n旅行や運転免許，資格スクールの申込や生協・共済などの加入手続き，学食パスの登録はこのお店のカウンターで受付しています．","本,購買,参考書,教科書,免許,生協,イヤホン","syoseki2");
            RoomEntity entity17 = new RoomEntity("情報科学図書室(西館1F)","50席","8:00~23:00","あり","情報科学部の自習室を兼ねた専門図書室です．学生が自由に使用できるiMac27インチモデルを設置．","落ち着いている,自習,広い,静か,充電できる,エアコン,本","library2");
            RoomEntity entity18 = new RoomEntity("ロビー(西館1F)","5席","8:00~23:00","なし","掲示エリアでは，教授からの連絡や変更された時間割など，学生たちが必要とする情報が得られます．","交流,雑談,掲示板,休憩スペース,自動販売機,待ち合わせ,時間割","lobbyimage2");
            RoomEntity entity19 = new RoomEntity("学生ラウンジ(西館1F)","80席","8:00~23:00","一部あり","学生たちがリラックスしたり，勉強したり，友人と交流したりするための共用スペースです．ガラス張りの明るいホールは，誰でも自由に利用できる学生の憩いの空間です．","勉強,雑談,掲示板,休憩スペース,交流,待ち合わせ,自習","");
            RoomEntity entity20 = new RoomEntity("GBC(西館1F)","-席","2限~5限(平日)","あり","講義や課題の内容，学生生活について，気軽に相談できる場所を提供しています．また，どの時間帯に行っても必ず教員が1～2名います．運営主体は情報科学部のSAであり，試験前の勉強会や，先生方との懇談会を企画しています．","相談スペース,勉強,懇談会,学生サポート,コミュニティ,交流,テスト返却","gbc2");
            RoomEntity entity21 = new RoomEntity("マルチユースホール(東館1F)","50席","8:00~23:00","あり","学生たちがリラックスしたり，勉強したり，友人と交流したりするための共用スペースです．\n","わいわい,お昼ご飯,掲示板,休憩スペース,自動販売機,イベント,自習","mult2");
            RoomEntity entity22 = new RoomEntity("食堂(東館B1)","520席","11:00~15:00(平日)","なし","約520席ある東館食堂は，スクールカラーのオレンジと青，DNAの螺旋構造をイメージさせる階段が特徴的な空間です．\n","わいわい,ラーメン,カウンター,食堂,電子マネー,生協,お昼ご飯","syokudou2");
            RoomEntity entity23 = new RoomEntity("ミュージアム小金井(西館1F)","-席","8:00~23:00","なし","法政理工系の研究・教育の過去・現在・未来を展示する場です．常設的には「法政理工系の軌跡」を展示しています．\n「法政理工系の軌跡」\n法政理工系の起源は，法政大学航空工業専門学校の創設(1944年）です．1945年終戦による法政工業専門学校への再編後，1950年に法政大学工学部が設酒され，法政理工系は新たな出発をします．その後は何度もの校舎移転を経て1964年に小金井キャンパスが開設．法政理工系はこの地で大きな発展を遂げていきます．","展示,歴史,モニター,研究,自動販売機,待ち合わせ,談笑","museum2");
            RoomEntity entity24 = new RoomEntity("視聴覚室(西館1F)","90席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","audio2");
            RoomEntity entity25 = new RoomEntity("W201(西館2F)","63席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w201image2");
            RoomEntity entity26 = new RoomEntity("W202(西館2F)","130席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w202image2");
            RoomEntity entity27 = new RoomEntity("W203(西館2F)","130席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w203image2");
            RoomEntity entity28 = new RoomEntity("W204(西館2F)","94席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w204image2");
            RoomEntity entity29 = new RoomEntity("W311(西館3F)","80席","8:00~23:00","あり","豊富な座席を備えた広々とした教室です．エアコンも完備しており，快適に学習することができます．","授業,落ち着いている,自習,広い,課題,充電できる,エアコン","w311image2");
            dao.insert(entity1);dao.insert(entity2);dao.insert(entity3);dao.insert(entity4);dao.insert(entity5);dao.insert(entity6);dao.insert(entity7);
            dao.insert(entity8);dao.insert(entity9);dao.insert(entity10);dao.insert(entity11);dao.insert(entity12);dao.insert(entity13);dao.insert(entity14);
            dao.insert(entity15);dao.insert(entity16);dao.insert(entity17);dao.insert(entity18);dao.insert(entity19);dao.insert(entity20);dao.insert(entity21);
            dao.insert(entity22);dao.insert(entity23);dao.insert(entity24);dao.insert(entity25);dao.insert(entity26);dao.insert(entity27);dao.insert(entity28);dao.insert(entity29);
        }
    }

    private void loadinfodb(){
        InformationDAO dao = dbInstance.informationdao();
        infolist = dao.getAll();

        if(infolist.size() == 0){ //テーブルの初期データ
            InformationEntity entity10 = new InformationEntity("パソコン・スマートフォン等利用時の注意","パソコン・スマートフォン等利用時の注意ポイントについて\n\n標題の件につきまして、秋学期開始を機に気にかけておきたいポイントをお知らせいたします。不注意によって「情報セキュリティインシデント」を発生させることがないよう、普段の生活の中で以下のポイントに留意してください。\n\n☆ポイント１　電子メールの送受信☆\n日常的に活用している電子メールだからこそ、あらゆる脅威にさらされています。\n特に以下のような点に注意を払うように心掛けてください。\n（１）身に覚えのない相手からや、少しでも怪しいと感じる内容のメールを受信したら、メール本文に記載されているURLや、メールに添付されているファイルは絶対に開かない。また、メールそのものも削除する。※削除する前に「迷惑メールの報告」機能を利用することも推奨します。\n（２）メール送信時には送信先のメールアドレスが誤っていないか入念に確認する。\n（３）たくさんの人にメールを同時に送信せざるを得ない時は、「To」「CC」「BCC」が適切に設定されているか、十分に気を付ける。\n（４）個人情報のやり取りが必要な時はできる限りメール添付を避け、GoogleDrive等を活用する。やむを得ずメール添付する際は容易に推測されないパスワードでファイルを暗号化し、復号用パスワードはメール本文に記載しない。※「個人情報」とは一般に、氏名・住所・電話番号・メールアドレス等個人を特定することが可能な情報を指します。\n\n☆ポイント２　データの管理☆\nクラウドサービスの普及により、大容量のデータをクラウドサービスにアップロードして保管することができるようになりました。他者とのデータのやり取りも簡単にできるサービスもあります。ただし、データ漏えいリスクも上がることになります。ファイル保存時には共有範囲の設定を入念に確認し、不必要に公開範囲を広げないようにしましょう。また、とりわけ重大な機密情報（個人情報に限りません）が含まれるデータを保存する場合などには、データの内容に応じた適切な保存場所・保存方法を選択してください。データ保存時に暗号化することも有効な手段です。\n\n☆ポイント３　ウイルス感染☆\nウイルス対策ソフトが表示したメッセージは、無視せずに内容をよく確認してください。特に、学内で使用している時にメッセージが表示された場合は、可能な限り速やかにHOSEI-CSIRTまでご相談ください。ご相談いただいた内容に応じて被害拡大防止等の措置を講じていただくことになります。ウイルス感染したまま使い続けると、ID・パスワード情報や保存されているデータ（クレジットカード情報等も含まれる場合があります）が盗まれてしまったり、その端末が乗っ取られたまま悪用されたりする危険性があります。大学の統合認証ID・パスワードが盗まれてしまうとなりすまし行為により大学全体のICT環境に大きな損害を与えてしまう恐れもあります。十分注意してください。\n\n☆ポイント４　サポート詐欺にご注意ください☆\nパソコンの画面に「ウイルスに感染しています。サポート窓口に電話してください」のような\n不安をあおるメッセージが表示されることがあります。これは「サポート詐欺」と言われる犯罪行為の典型例です。HOSEI-CSIRTでもサポート詐欺に関する相談を複数受けています。金銭を要求されたりウイルスをインストールさせられたりする危険性が高いので、絶対に電話はしないでください。心配になったらまずはHOSEI-CSIRTにお気軽にご相談ください。",0,"その他","2023/10/4 14:04","","");
            InformationEntity entity9 = new InformationEntity("アルゴリズム入門，教室変更のお知らせ","アルゴリズム入門を受講している皆さん\n\n本日の授業より、実施教室を以下の通り変更します。教室間違いのないようご注意ください。\n 【変更前】AL1　→　【変更後】W101",0,"重要","2023/10/4 12:20","W101(西館1F)","w101image");
            InformationEntity entity8 = new InformationEntity("「ダムへのアプローチ」参加者募集のお知らせ","学習ステーションでは、「Lステゼミ」と呼ばれるミニ講座を不定期で開催しています。\n\n\n教員などが講師となって行うショートゼミで、語学・教養・専門と、話題は多岐にわたります。\n\n学士力として期待される「知識・理解」を深めることを主目的として、気軽に学ぶ機会を提供します。\n\n皆さまのご参加をお待ちしています！！\n\n\n----------------------------------------------------------------------------------------------------------\n\n★ダムへのアプローチ★\n\n\nダムは自然破壊の象徴のように扱われますが、私達の生活に欠かせない施設であることも事実です。\n\nダムに映し出される科学技術の光と影を眺めながら人工建造物としてのダムへの理解を深めます。\n\n\n\n\n開催日時：2023年10月18日（水）13：30～14：50\n\n\n開催場所：Zoomで行います。（申込者にのみURLをご案内します）\n\n\n申　込　：以下のフォームよりお申し込みください。\n\n\nhttps://forms.gle/Dc1Uq9AzP7XzPxQ49\n\n\n申込締切：10月16日（月）17:00\n\n\n講　師　：道奥　康治（デザイン工学部教授）　　",0,"イベント","2023/10/4 11:05","","");
            InformationEntity entity7 = new InformationEntity("（英語強化プログラム）受講生募集について","グローバル教育センターよりお知らせです。\nERP（英語強化プログラム）は、すべての授業を英語で実施する少人数制の英語授業で、3キャンパスにて対面授業（一部オンライン）にて開講いたします。\n\n\n秋学期のERP授業は、9月21日（木）より授業開始となり、あわせて専用サイトで受講申込を受け付けます。申込締切は9月26日(火）24時（夜12時）となります。\n\n（ERP募集要項等）https://www.global.hosei.ac.jp/programs/oncampus/erp/\n（ERP専用申込サイト）https://system-op.com/erp/（9月21日（木）より受付開始）",0,"その他","2023/9/19 16:20","","");
            InformationEntity entity6 = new InformationEntity("卒業研究中間発表会のお知らせ","情報科学部4年生の皆さん：\n\n卒業研究中間発表会（9月30日（土）実施)について詳細をお知らせします。\n「卒研サイト」の「中間発表会」のページに、発表会および提出物の情報を掲載いたしましたので、各自漏れなく確認してください。\n発表会の「グループ別発表順」については、このお知らせに添付いたします。各自ご確認をお願いいたします。\n\n　●（卒研サイト）：　https://cis.k.hosei.ac.jp/sotsuken/\n　●（卒研サイト ＞ 中間発表会）：　https://cis.k.hosei.ac.jp/sotsuken/undergraduate/midterm-presentation/\n\nまた、注意事項として発表会当日にギリギリに来る学生が毎年散見されます。\n発表会の開始時刻の10分前まで（時間厳守）に各自発表できるように準備してください。\n当日朝に急遽発表順が変更になった場合にも対応（発表）できるように教室で準備しておいてください。\n\n以上",0,"重要","2023/9/15 11:00","ロビー(西館1F)","lobbyimage");
            InformationEntity entity5 = new InformationEntity("2023年度秋学期の履修登録が開始されます","情報科学部生の皆さん：\n\n履修に関する重要なお知らせをいたしますので、必ず確認してください。\n\n\n０　重要事項について\n（１）履修登録については自己責任です。取消忘れや登録漏れ、確認漏れなどはご注意ください。\n\n（２）英語科目や受講指定科目は学部で履修登録を実施します。\n　　　しかしながら、履修登録されている受講可能科目の確認やクラスの確認などは自己責任です。\n　　　何か間違いやご不明な点等がある場合には履修登録期間中に情報科学部までお問い合わせください。\n\n（３）毎学期ガイダンスでお伝えしておりますが、法政大学GmailやWeb掲示板は\n　　　少なくとも１日１回は確認するようにしてください。\n　　　履修関連のお知らせなどは全て法政大学GmailやWeb掲示板からお知らせします。\n　　　確認漏れがないようにしてください。\n\n\n１　履修登録について\n　9月16日（土）10：00より、履修登録（本登録）が開始となります。\n　※履修登録期間内に、履修登録（本登録）しないと成績評価の対象(単位が取得できません)となりません！\n　\n　改めて、以下をご確認の上、登録期間内に必ず手続きを行ってください。\n　\n　● 法政大学情報システム（情報ポータル）：\n　　　https://www.as.hosei.ac.jp/\n\n　● 2023年度秋学期授業・履修登録関連スケジュール\n　　　https://cis.hosei.ac.jp/wp-content/uploads/2023/08/2023f_course_registration.pdf\n\n　● 2023年度時間割、2023年度履修ガイド\n　　　https://cis.hosei.ac.jp/info/students/#001\n\n　● Web履修登録の手引き\n　　（PC版）\n　　　https://cis.hosei.ac.jp/wp-content/uploads/2022/03/web-registration-manual.pdf\n　　（スマホ版）\n　　　https://cis.hosei.ac.jp/wp-content/uploads/2021/03/smartphone_manual.pdf",0,"重要","2023/9/15 16:53","","");
            InformationEntity entity4 = new InformationEntity("卒業生による就職に向けたキャリア相談会","理系学部・研究科の皆さん\n\n卒業生による就職に向けたキャリア相談会の開催が，いよいよ今週の金曜日となりましたので，再度ご案内します．\n\nApple watch, Nintendo swithch, Sony ワイヤレスイヤホン，スターバックスギフトセットなど，豪華景品の当たる大抽選会も開催されます． \n\n事前申し込みをしていない方でも参加できますので，友達と一緒に是非参加してください．\n当日，１２：３０に東館１階 マルチユース ホールにて受付をしてください．\n\n＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝\n\n[以下再掲]\n\n毎年恒例のキャリア相談会（注１）が，今年は９月１５日（金）に理系同窓会（注２）の協力のもとに実施されます．\n先輩や企業の採用担当の方から就職にあたってのアドバイスや職場の様子など，生の声を聴くことが出来るまたとない機会です．\n是非，参加してください．特に，就職活動中の皆さんや，そろそろ就職活動を始めようとしている皆さんにとっては，先輩からの支援が得られ，志望する企業の内定に結びつく大きなチャンスになります．\n\n　また，当日は， QRコードの発明者である原昌宏氏（本学卒業生）による記念講演や，豪華賞品の当たる大抽選会，祝賀会・懇親会（参加費無料）も開催されます．是非，そちらにも参加してください．\n\n　参加費は全て無料で，服装も自由です．当日，受付で記帳するだけで参加できます．\n尚，参加予定人数を把握するため，事前に下記のグーグルフォームより登録をお願いします．\n\nお申し込みフォーム（締め切り9/5）　　https://forms.gle/CysBxgBqyJf8uyX56\n\n◇ 開 催 日　2023 年 9 月 15 日（金） 法政大学小金井キャンパス\n\n◇ 開 場・受 付　　　　　　　　　   \n12:30～　 　  (東館１階 マルチユース ホール)\n\n◇ キャリア相談会  　　　　　　　   \n13:00-15:00　 (東館３階小体育室)\nピザやお菓子を食べながら，立食パーティー形式で懇談します．\n抽選会の抽選券もここで配布されます．\n\n◇ 名誉博士学位授与記念講演会 　    \n15:30-16:30   (東館２階体育館)\n\n◇ 理系コンソーシアム（注３）設立記念式典\n16:45-17:15   (東館２階体育館)\n\n◇ 祝賀会・懇親会\n18:30-20:30   (東館地下1階生協食堂)\n※アルコールが提供されますが，未成年者は絶対に飲酒しないでください．\n\n\n＜主な参加予定企業（内定者のアドバイザーを含む）＞\nJR東海， NTT研究所，日産自動車株式会社，本田技研工業株式会社，キヤノン株式会社，日本IBM株式会社，株式会社日立製作所，株式会社東芝，キオクシアシステムズ株式会社，オリエンタルモーター株式会社，株式会社大林組，株式会社関電工，北野建設株式会社株式会社，株式会社大和総研，三菱ケミカル株式会社，三菱マテリアル株式会社，ALSOK，北海道庁，岩手県庁，神奈川県庁，長野県庁，他数十社の参加を予定        ",0,"イベント","2023/9/12 12:00","マルチユースホール(東館1F)","mult");
            InformationEntity entity3 = new InformationEntity("毎日企業説明会 in 小金井キャンパス","8月25日(金)から9月8日(金)まで，14社の参加による「学内で企業と会える毎日企業説明会 in小金井キャンパス」が開催されています．\n\nhttps://www.hosei.ac.jp/careercenter/info/article-20230808165212/\n\n普段から求人案内等をいただいているアクモス株式会社様も以下の通り説明会を開催されるとのことです．\n\n日時：9月8日(金) 13:00〜\n場所：管理棟3階英会話教室\n申込：キャリア就職システム\n(「支援行事の予約」での事前予約制)\n\n他の企業も含め，参加を検討してください．",0,"イベント","2023/9/4 16:55","W102(西館1F)","w102image");
            InformationEntity entity2 = new InformationEntity("学内選考がある奨学金のお知らせ","★【学内選考のある民間奨学金】とは、各財団への推薦枠が定められている奨学金のことです。大学にて推薦者を決定し財団等の選考を受けます。募集要項・申請書類は財団HP等からダウンロードしてください。ダウンロードができないものは別途配布いたします。\n★申請締切は要項に記載されているものでなく、このお知らせでご案内している日時になりますのでご注意ください（学内で選考を行うため）。要項に記載されているもの以外に追加提出いただく書類はこのお知らせの備考欄に記載していますのでご注意ください。願書に写真を添付する場合はスーツ着用の写真を使用してください。\n「推薦書」は、指導教員に作成していただいてください（難しい場合は事前に早めに学生生活課に相談すること）。\n「法政大学総長あての誓約書」も、このお知らせに添付している書式で作成してください。\n★学内選考のある奨学金申請希望者は、事前に小金井学生生活課（管理棟2階）まで申し出てください（メール連絡可、　メールの場合はkgakusei@hosei.ac.jp　宛てに「民間奨学金申請届」（エクセルファイル）を添付してお送りください。\n　追加があればその都度送ってください）。※連絡後に申請を取りやめても問題ありません。\n★原則として、成績優秀でありながら経済的事情により学費の支払いが困難な方が対象です。奨学金ごとの対象・要件と「民間奨学財団奨学生選考基準」を確認し、基準に当てはまる方のみ申請してください。また、特に明記がない限り、給付（貸与）期間は大学の正規の最短修業期間です。\n追加で募集があれば、随時更新していきます。8月28日追加があります。",0,"その他","2023/8/28 12:02","","");
            InformationEntity entity1 = new InformationEntity("就職活動全般に関するアンケート回答のお願い","学部4年生、修士2年生　各位\n※既に回答頂いた方も含め、このメールを送信しております。\n\nお世話になります。\n法政大学キャリアセンターです。\n\n日頃よりキャリアセンターをご利用頂き、誠に有難うございます。\n\nこの度、キャリアセンターでは、今後就職活動を予定している本学就活生へのより良い支援の提供を目的に、進路報告登録が完了した方を対象に、アンケートにご協力頂きたくご連絡を差し上げました。\n\nつきましては、ご多忙中誠に恐れ入りますが、以下のURLより、皆様の率直なご意見・ご要望をお聞かせください。\n\n※短い時間で完了しますので、是非ともご協力をお願いいたします。\n------------------------------------------------------------\n◆Googleフォーム：https://forms.gle/sz4dLrhQejyGn9EZ6\n◆所要時間：約1～5分\n◆回答期限：2023年9月4日（月）9：00まで",0,"その他","2023/8/27 13:53","","");
            dao.insert(entity1);dao.insert(entity2);dao.insert(entity3);dao.insert(entity4);dao.insert(entity5);dao.insert(entity6);dao.insert(entity7);dao.insert(entity8);dao.insert(entity9);dao.insert(entity10);
        }
    }

    private void loadgroupdb(){
        GroupDAO dao = dbInstance.groupdao();
        grouplist = dao.getAll();

        if(grouplist.size() == 0){
            GroupEntity entity1 = new GroupEntity(0,0,"プログラミング勉強会","22K032,22K190,22K323,22K374,22K904,22K554,22K117,21K461,21K006,21K041,21K482,21K143,21K656,21K341,21K712,20K214,20K142,20K534","プログラミング講義の情報共有や質問のためのグループです．Python,C++,Java等々．受講者だけでなく，履修済みの方でもぜひ参加してください！！","programming2");
            GroupEntity entity2 = new GroupEntity(0,0,"英語勉強モチベーション維持","22K032,22K190,22K323,22K374,22K904,22K554,22K117,21K461,21K006,21K041,21K482,21K143,21K656,21K341,21K712,20K214,20K142,20K534,22K032,22K190,22K323,22K374,22K904,22K554,22K117,21K461,21K006,21K041,21K482,21K143,21K656,21K341,21K712,20K214,20K142,20K534","英語勉強のモチベーションを維持するために学習者同士で勉強内容を報告し合うグループです．","english2");
            GroupEntity entity3 = new GroupEntity(0,1,"🎮ゲーム仲間🎮","22K554,22K117,21K461,21K006,21K041,21K482,21K143,21K656,21K341,21K712,20K214,20K142,20K534","その名の通りゲームを共にする仲間のコミュニティ","game2");
            GroupEntity entity4 = new GroupEntity(0,0,"Bリーグ バスケ観戦","22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,22K032,22K190,22K323,22K374,22K904,22K032,22K190,22K323,22K374,22K904,22K554,22K117,21K461,21K006,21K041,21K482,21K143,21K656,21K341,21K712,20K214,20K142,20K534","Bリーグを中心に，日本代表戦，NBAなどバスケ観戦に関する話題を楽しむグループです！！","bask2");
            GroupEntity entity5 = new GroupEntity(0,0,"UniVerse ボドゲ部","22K032,22K190,22K323,22K374,22K904,21K656,21K341,21K712,20K214,20K142,20K534","ボードゲームやるだけ","boardgame2");
            GroupEntity entity6 = new GroupEntity(0,1,"歌ってみた界隈","22K032,22K190,22K323,22K374,22K904,22K554,22K117,21K461,21K006,21K041,21K482,21K143,21K656,22K032,22K190,22K323,21K341,21K712,20K214,20K142,20K534","歌ってみたに関わる歌い手さん，MIX師さん，動画師さん，絵師さん，Vtuberさん達の交流や情報交換の場になればと思っています．","utaite2");
            dao.insert(entity1);dao.insert(entity2);dao.insert(entity3);dao.insert(entity4);dao.insert(entity5);dao.insert(entity6);
        }
    }

    private void loadpostsdb(){
        PostsDAO dao = dbInstance.postsdao();
        postslist = dao.getAll();

        if(postslist.size() == 0){
            PostsEntity entity9 = new PostsEntity(2,"22K032","新メニューのとんこつラーメン美味しかった！","食堂(東館B1)","syokudou","ramen","7:46 11/1");
            PostsEntity entity10 = new PostsEntity(1,"22K562","今日は１限から５限まで授業があって大変だ～","","","","7:32 11/1");
            PostsEntity entity14 = new PostsEntity(4,"21K041","2年次の時間割共有したのでよかったら参考にしてください","","","","22:10 10/31");
            PostsEntity entity3 = new PostsEntity(2,"20K190","こちらの教室でサイフの忘れ物を見つけました\n教授に届けています","W101(西館1F)","w101image","wallet","13:34 11/1");
            PostsEntity entity19 = new PostsEntity(0,"22K001","今日の線形代数の講義，本当に難しかった，，","","","","13:11 10/31");
            PostsEntity entity16 = new PostsEntity(5,"22K563","サークルどこに入ろうかな","","","","18:35 10/31");
            PostsEntity entity5 = new PostsEntity(2,"20K323","バスケを観戦する新しいグループを作りました！","","","","10:32 11/1");
            PostsEntity entity12 = new PostsEntity(1,"22K134","キャンパス内で一番広い食堂はどこですか？","","","","7:01 11/1");
            PostsEntity entity6 = new PostsEntity(7,"21K456","就活の支援をしてくれる場所ってどこでしたっけ？","","","","10:22");
            PostsEntity entity8 = new PostsEntity(3,"21K767","アクティブラーニングラボとアクティブラーニング教室は違いますか？西館にあるのはどっちですか？","","","","8:12 11/1");
            PostsEntity entity2 = new PostsEntity(8,"21K321","授業の教科書はどこで買えますか？","","","","14:57 11/1");
            PostsEntity entity18 = new PostsEntity(3,"20K436","情報科学部です\n線形代数の課題プリントってどこで返却してますか？","","","","13:21 10/31");
            PostsEntity entity15 = new PostsEntity(9,"22K390","ここは月曜だと３限まで空き教室なので，朝早く来て勉強しています！","W102(西館1F)","w102image","","20:56 10/31");
            PostsEntity entity1 = new PostsEntity(10,"20K003","ここは西館入口から近いのにあまり人がいないので，穴場の勉強スポットです！！","情報科学図書室(西館1F)","library","","15:00 11/1");
            PostsEntity entity20 = new PostsEntity(4,"22K554","ここは席が多くてリラックスしながら勉強できます\nお昼は賑やかですが，午後は真面目に勉強してる人が多いので，いつも利用しています","マルチユースホール(東館1F)","mult","","10:44 10/31");
            PostsEntity entity17 = new PostsEntity(2,"21K133","実は一人用のカウンター席が角にあって，コンセントも使えます\n授業前の空き時間に勉強しています","学生ラウンジ(西館2F)","","","17:24 10/31");
            PostsEntity entity4 = new PostsEntity(26,"21K060","私はいつもここで友達と勉強しています！\n席数が多くて，少し賑やかなので友達と相談しながら気楽に勉強できます\nGBCが目の前にあるのでわからないときはすぐに質問できますよ！","学生ラウンジ(西館1F)","","","12:25 11/1");
            PostsEntity entity7 = new PostsEntity(21,"21K889","集中して勉強するならここ一択\n300席もあるし，図書室だからいつも静かです","小金井図書館(南館1F)","tosyokan","","8:24 11/1");
            PostsEntity entity11 = new PostsEntity(4,"20K322","ホワイトボードがあって，グループワークをするのに最適な場所です！一人で自習してる人もいます","ラーニングコモンズ(南館1F)","learning","","7:09 11/1");
            PostsEntity entity13 = new PostsEntity(5,"21K564","プログラミングの勉強をするときはいつもここです笑\n教授に教わりながら課題をしています","GBC(西館1F)","gbc","","7:01 11/1");
            dao.insert(entity20);dao.insert(entity19);dao.insert(entity18);dao.insert(entity17);dao.insert(entity16);dao.insert(entity15);dao.insert(entity14);dao.insert(entity13);dao.insert(entity12);dao.insert(entity11);
            dao.insert(entity10);dao.insert(entity9);dao.insert(entity8);dao.insert(entity7);dao.insert(entity6);dao.insert(entity5);dao.insert(entity4);dao.insert(entity3);dao.insert(entity2);dao.insert(entity1);
        }
    }

    private void loadrepliesdb(){
        RepliesDAO dao = dbInstance.repliesdao();
        replieslist = dao.getAll();

        if(postslist.size() == 0){
            RepliesEntity entity1 = new RepliesEntity(18,"20K432","ありがとうございます！！私のです！","","","14:10 11/1");
            RepliesEntity entity2 = new RepliesEntity(16,"20K126","参加させていただきます！","","","12:10 11/1");
            RepliesEntity entity3 = new RepliesEntity(20,"20K665","ここ僕もお気に入りです！","","","18:10 11/1");
            dao.insert(entity1);dao.insert(entity2);dao.insert(entity3);
        }
    }

    private void deleteRoomTable(){
        RoomDAO dao = dbInstance.roomdao();
        dao.deleteall();
    }

    private void deleteInfoTable() {
        InformationDAO dao = dbInstance.informationdao();
        dao.deleteAll();
    }

    private void deleteGroupTable() {
        GroupDAO dao = dbInstance.groupdao();
        dao.deleteAll();
    }

    private void deletePostsTable(){
        PostsDAO dao = dbInstance.postsdao();
        dao.deleteAll();
    }

    private void deleteRepliesTable(){
        RepliesDAO dao = dbInstance.repliesdao();
        dao.deleteAll();
    }

    private String getMessageKeyForGroup(String groupName) {
        return "chat_messages_" + groupName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbInstance.close(); // データベースを閉じる
    }

    @Override
    public void onClick(View view){  //ボタンをクリックしたときの挙動
        if(view.getId() == R.id.mapIcon) {  //switch使うとエラー(if elseを使う)
            Intent intentlevel = new Intent(getApplication(),MainActivity.class); //画面遷移(MainActivity→GameLevel)
            startActivity(intentlevel);
        } else if(view.getId() == R.id.searchIcon){
            Intent intent = new Intent(getApplication(),Search.class);
            startActivity(intent);
        } else if(view.getId() == R.id.snsIcon){
            Intent intent = new Intent(getApplication(),FeedTop.class);
            startActivity(intent);
        } else if(view.getId() == R.id.infoIcon){
            Intent intent = new Intent(getApplication(),Information.class);
            startActivity(intent);
        } else if(view.getId() == R.id.mypageIcon){
            Intent intent = new Intent(getApplication(),MypageTop.class);
            startActivity(intent);
        } else if(view.getId() == R.id.select9){
            deleteAndReenterUsername();
            initData();
        } else if(view.getId() == R.id.select2){
            Intent intent = new Intent(getApplication(),TimeTable.class);
            startActivity(intent);
        } else if(view.getId() == R.id.select3){
            Intent intent = new Intent(getApplication(),Text.class);
            startActivity(intent);
        } else if(view.getId() == R.id.select4){
            Intent intent = new Intent(getApplication(),Work.class);
            startActivity(intent);
        } else if(view.getId() == R.id.select5){
            Intent intent = new Intent(getApplication(),Question.class);
            startActivity(intent);
        } else if(view.getId() == R.id.select6){
            Intent intent = new Intent(getApplication(),Syllabus.class);
            startActivity(intent);
        }
    }
}