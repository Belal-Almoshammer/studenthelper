package com.pandasoft.studenthelper.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pandasoft.studenthelper.DAOs.DaoBook;
import com.pandasoft.studenthelper.DAOs.DaoEducationalLevel;
import com.pandasoft.studenthelper.DAOs.DaoInstitutes;
import com.pandasoft.studenthelper.DAOs.DaoNotifications;
import com.pandasoft.studenthelper.DAOs.DaoNotificationsTeacher;
import com.pandasoft.studenthelper.DAOs.DaoQuestion;
import com.pandasoft.studenthelper.DAOs.DaoQuiz;
import com.pandasoft.studenthelper.DAOs.DaoStudent;
import com.pandasoft.studenthelper.DAOs.DaoSubject;
import com.pandasoft.studenthelper.DAOs.DaoSubjectChapters;
import com.pandasoft.studenthelper.DAOs.DaoSubjectsLevel;
import com.pandasoft.studenthelper.DAOs.DaoTeacher;
import com.pandasoft.studenthelper.DAOs.DaoTeacherCourses;
import com.pandasoft.studenthelper.DAOs.DaoTipsAndAdvice;
import com.pandasoft.studenthelper.DAOs.DaoUniversities;
import com.pandasoft.studenthelper.DAOs.DaoUser;
import com.pandasoft.studenthelper.DAOs.DaoVideo;
import com.pandasoft.studenthelper.DAOs.DaoWebsites;
import com.pandasoft.studenthelper.Entities.EntityBook;
import com.pandasoft.studenthelper.Entities.EntityEducationalLevel;
import com.pandasoft.studenthelper.Entities.EntityInstitutes;
import com.pandasoft.studenthelper.Entities.EntityNotifications;
import com.pandasoft.studenthelper.Entities.EntityNotificationsTeacher;
import com.pandasoft.studenthelper.Entities.EntityQuestion;
import com.pandasoft.studenthelper.Entities.EntityQuiz;
import com.pandasoft.studenthelper.Entities.EntityReplayMessage;
import com.pandasoft.studenthelper.Entities.EntityStudent;
import com.pandasoft.studenthelper.Entities.EntitySubject;
import com.pandasoft.studenthelper.Entities.EntitySubjectChapters;
import com.pandasoft.studenthelper.Entities.EntitySubjectsLevel;
import com.pandasoft.studenthelper.Entities.EntityTeacher;
import com.pandasoft.studenthelper.Entities.EntityTeacherCourses;
import com.pandasoft.studenthelper.Entities.EntityTipsAndAdvice;
import com.pandasoft.studenthelper.Entities.EntityUniversities;
import com.pandasoft.studenthelper.Entities.EntityUser;
import com.pandasoft.studenthelper.Entities.EntityVideo;
import com.pandasoft.studenthelper.Entities.EntityWebsites;
import com.pandasoft.studenthelper.Tools.MyToolsCls;


@Database(entities = {
        EntityBook.class, EntityEducationalLevel.class,
        EntityInstitutes.class, EntityNotifications.class, EntityNotificationsTeacher.class,
        EntityQuestion.class, EntityReplayMessage.class, EntityStudent.class,
        EntitySubjectChapters.class, EntitySubject.class, EntitySubjectsLevel.class,
        EntityTeacher.class, EntityTeacherCourses.class, EntityTipsAndAdvice.class,
        EntityUniversities.class, EntityUser.class, EntityVideo.class, EntityWebsites.class, EntityQuiz.class
}, version = 45, exportSchema = false)
public abstract class MyRoomDatabase extends androidx.room.RoomDatabase {
    private static Context mContext;
    private static MyRoomDatabase instance;
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            if (mContext != null) {
                MyToolsCls.setUserToken(mContext);
            }
            FirebaseMessaging.getInstance().subscribeToTopic("STUDENT_HELPER_NOTIFICATIONS");
            new PopulateDataAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    // Singleton Method to get instance
    public static synchronized MyRoomDatabase getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyRoomDatabase.class,
                    "db_student_helper") // database name
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    // All Data Access Objects (DAOs) of (Entities) tables
    public abstract DaoBook daoBook();

    public abstract DaoEducationalLevel daoEducationalLevel();

    public abstract DaoInstitutes daoInstitutes();

    public abstract DaoNotifications daoNotifications();

    public abstract DaoNotificationsTeacher daoNotificationsTeacher();

    public abstract DaoQuestion daoQuestion();

    public abstract DaoStudent daoStudent();

    public abstract DaoSubjectChapters daoSubjectChapters();

    public abstract DaoSubject daoSubjects();

    public abstract DaoSubjectsLevel daoSubjectsLevel();

    public abstract DaoTeacher daoTeacher();

    public abstract DaoTeacherCourses daoTeacherCourses();

    public abstract DaoTipsAndAdvice daoTipsAndAdvice();

    public abstract DaoUniversities daoUniversities();

    public abstract DaoUser daoUser();

    public abstract DaoVideo daoVideo();

    public abstract DaoWebsites daoWebsites();

    public abstract DaoQuiz daoQuiz();

    private static class PopulateDataAsyncTask extends AsyncTask<Void, Void, Void> {

        MyRoomDatabase database;

        public PopulateDataAsyncTask(MyRoomDatabase db) {
            this.database = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // ================================================= insert educational levels
            this.database.daoEducationalLevel().insert(new EntityEducationalLevel("10", "الأول الثانوي"));
            this.database.daoEducationalLevel().insert(new EntityEducationalLevel("11", "الثاني الثانوي"));
            this.database.daoEducationalLevel().insert(new EntityEducationalLevel("12", "الثالث الثانوي"));
            // ================================================= add all subjects
            this.database.daoSubjects().insert(new EntitySubject("1", "القرآن الكريم", "#2b242b", "quran5"));
            this.database.daoSubjects().insert(new EntitySubject("2", "التربية الاسلامية", "#0f224a", "quran6"));
            this.database.daoSubjects().insert(new EntitySubject("3", "اللغة العربية", "#573200", "arabic3"));
            this.database.daoSubjects().insert(new EntitySubject("4", "الرياضيات", "#347b98", "math2"));
            this.database.daoSubjects().insert(new EntitySubject("5", "التاريخ", "#6b4102", "history1"));
            this.database.daoSubjects().insert(new EntitySubject("6", "المجتمع", "#66b032", "nation3"));
            this.database.daoSubjects().insert(new EntitySubject("7", "الجغرافيا", "#347b98", "goegraphy1"));
            this.database.daoSubjects().insert(new EntitySubject("8", "الفيزياء", "#3d00a4", "physical1"));
            this.database.daoSubjects().insert(new EntitySubject("9", "الكيمياء", "#56013e", "chemistry2"));
            this.database.daoSubjects().insert(new EntitySubject("10", "الأحياء", "#fbbc00", "biology3"));
            this.database.daoSubjects().insert(new EntitySubject("11", "English", "#a61a4b", "english1"));

            // الصف الأول الثانوي =================================================
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("1", "10", "1"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("2", "10", "2"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("3", "10", "3"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("4", "10", "4"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("5", "10", "5"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("6", "10", "6"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("7", "10", "7"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("8", "10", "8"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("9", "10", "9"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("10", "10", "10"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("11", "10", "11"));
            //الصف الثاني الثانوي ===================================================
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("12", "11", "1"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("13", "11", "2"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("14", "11", "3"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("15", "11", "4"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("16", "11", "8"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("17", "11", "9"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("18", "11", "10"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("19", "11", "11"));
            //الصف الثالث الثانوي ====================================================
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("20", "12", "1"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("21", "12", "2"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("22", "12", "3"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("23", "12", "4"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("24", "12", "8"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("25", "12", "9"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("26", "12", "10"));
            this.database.daoSubjectsLevel().insert(new EntitySubjectsLevel("27", "12", "11"));


            return null;
        }
    }
}

