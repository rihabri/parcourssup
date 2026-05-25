package com.example.parcourssup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "parcourssup.db";
    private static final int DB_VERSION = 4;

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static final String TABLE_USERS = "users";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ECOLES = "ecoles";
    public static final String TABLE_USER_ECOLES = "user_ecoles";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "prenom TEXT," +
                "nom TEXT," +
                "age TEXT," +
                "telephone TEXT," +
                "ville TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "pack TEXT DEFAULT ''," +
                "statut_paiement TEXT DEFAULT 'en_attente'," +
                "date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "pack TEXT," +
                "prix TEXT," +
                "statut TEXT," +
                "date_commande DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE ecoles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT," +
                "nom_arabe TEXT," +
                "type TEXT," +
                "ville TEXT," +
                "prix TEXT," +
                "duree TEXT," +
                "diplome TEXT," +
                "filiere TEXT)");

        db.execSQL("CREATE TABLE user_ecoles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "ecole_id INTEGER," +
                "date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS ecoles");
        db.execSQL("DROP TABLE IF EXISTS user_ecoles");
        onCreate(db);
    }



    public long addUser(User user, String password) {
        ContentValues cv = new ContentValues();
        cv.put("prenom", user.getPrenom());
        cv.put("nom", user.getNom());
        cv.put("age", user.getAge());
        cv.put("telephone", user.getTelephone());
        cv.put("ville", user.getVille());
        cv.put("email", user.getEmail());
        cv.put("password", password);
        cv.put("pack", "");
        cv.put("statut_paiement", "en_attente");

        return getWritableDatabase().insert(TABLE_USERS, null, cv);
    }

    public boolean checkUser(String email, String password) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password});
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    public User getUserByEmail(String email) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM users WHERE email=?",
                new String[]{email});

        if (c.moveToFirst()) {
            User user = new User();
            user.setId(c.getLong(c.getColumnIndexOrThrow("id")));
            user.setPrenom(c.getString(c.getColumnIndexOrThrow("prenom")));
            user.setNom(c.getString(c.getColumnIndexOrThrow("nom")));
            user.setAge(c.getString(c.getColumnIndexOrThrow("age")));
            user.setTelephone(c.getString(c.getColumnIndexOrThrow("telephone")));
            user.setVille(c.getString(c.getColumnIndexOrThrow("ville")));
            user.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            user.setPack(c.getString(c.getColumnIndexOrThrow("pack")));
            user.setStatutPaiement(c.getString(c.getColumnIndexOrThrow("statut_paiement")));
            c.close();
            return user;
        }
        c.close();
        return null;
    }

    public User getUserById(long id) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM users WHERE id=?",
                new String[]{String.valueOf(id)});

        if (c.moveToFirst()) {
            User user = new User();
            user.setId(c.getLong(c.getColumnIndexOrThrow("id")));
            user.setPrenom(c.getString(c.getColumnIndexOrThrow("prenom")));
            user.setNom(c.getString(c.getColumnIndexOrThrow("nom")));
            user.setAge(c.getString(c.getColumnIndexOrThrow("age")));
            user.setTelephone(c.getString(c.getColumnIndexOrThrow("telephone")));
            user.setVille(c.getString(c.getColumnIndexOrThrow("ville")));
            user.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            user.setPack(c.getString(c.getColumnIndexOrThrow("pack")));
            user.setStatutPaiement(c.getString(c.getColumnIndexOrThrow("statut_paiement")));
            c.close();
            return user;
        }
        c.close();
        return null;
    }

    public int getUserCount() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM users", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM users", null);

        while (c.moveToNext()) {
            User u = new User();
            u.setId(c.getLong(c.getColumnIndexOrThrow("id")));
            u.setPrenom(c.getString(c.getColumnIndexOrThrow("prenom")));
            u.setNom(c.getString(c.getColumnIndexOrThrow("nom")));
            u.setAge(c.getString(c.getColumnIndexOrThrow("age")));
            u.setTelephone(c.getString(c.getColumnIndexOrThrow("telephone")));
            u.setVille(c.getString(c.getColumnIndexOrThrow("ville")));
            u.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            u.setPack(c.getString(c.getColumnIndexOrThrow("pack")));
            u.setStatutPaiement(c.getString(c.getColumnIndexOrThrow("statut_paiement")));
            list.add(u);
        }
        c.close();
        return list;
    }

    public boolean deleteUser(long id) {
        return getWritableDatabase()
                .delete("users", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public void updateUserPack(long userId, String pack) {
        ContentValues cv = new ContentValues();
        cv.put("pack", pack);
        getWritableDatabase().update("users", cv, "id=?",
                new String[]{String.valueOf(userId)});
    }

    public void updateStatutPaiement(long userId, String statut) {
        ContentValues cv = new ContentValues();
        cv.put("statut_paiement", statut);
        getWritableDatabase().update("users", cv, "id=?",
                new String[]{String.valueOf(userId)});
    }

    public String getUserPack(long userId) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT pack FROM users WHERE id=?",
                new String[]{String.valueOf(userId)});
        String pack = "";
        if (c.moveToFirst()) {
            pack = c.getString(0);
        }
        c.close();
        return pack;
    }

    // ── Récupérer le statut de paiement ──
    public String getStatutPaiement(long userId) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT statut_paiement FROM users WHERE id=?",
                new String[]{String.valueOf(userId)});
        String statut = "en_attente";
        if (c.moveToFirst()) {
            statut = c.getString(0);
        }
        c.close();
        return statut;
    }



    public long addOrder(Order o) {
        ContentValues cv = new ContentValues();
        cv.put("user_id", o.getUserId());
        cv.put("pack", o.getPack());
        cv.put("prix", o.getPrix());
        cv.put("statut", o.getStatut());
        return getWritableDatabase().insert("orders", null, cv);
    }

    public int getOrderCount() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM orders", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM orders", null);

        while (c.moveToNext()) {
            Order o = new Order();
            o.setId(c.getLong(0));
            o.setUserId(c.getLong(1));
            o.setPack(c.getString(2));
            o.setPrix(c.getString(3));
            o.setStatut(c.getString(4));
            list.add(o);
        }
        c.close();
        return list;
    }

    public void updateOrderStatut(long orderId, String statut) {
        ContentValues cv = new ContentValues();
        cv.put("statut", statut);
        getWritableDatabase().update("orders", cv, "id=?",
                new String[]{String.valueOf(orderId)});
    }

    public boolean deleteOrder(long id) {
        return getWritableDatabase()
                .delete("orders", "id=?", new String[]{String.valueOf(id)}) > 0;
    }



    public long addEcole(Ecole e) {
        ContentValues cv = new ContentValues();
        cv.put("nom", e.getNom());
        cv.put("nom_arabe", e.getNomArab());
        cv.put("type", e.getType());
        cv.put("ville", e.getVille());
        cv.put("prix", e.getPrix());
        cv.put("duree", e.getDuree());
        cv.put("diplome", e.getDiplome());
        cv.put("filiere", e.getFiliere());
        return getWritableDatabase().insert("ecoles", null, cv);
    }

    public List<Ecole> getAllEcoles() {
        List<Ecole> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM ecoles", null);

        while (c.moveToNext()) {
            Ecole e = new Ecole(
                    c.getString(1), c.getString(2), c.getString(3),
                    c.getString(4), c.getString(5), c.getString(6),
                    c.getString(7), c.getString(8));
            e.setId(c.getLong(0));
            list.add(e);
        }
        c.close();
        return list;
    }

    public int getEcoleCount() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM ecoles", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public boolean updateEcole(Ecole e) {
        ContentValues cv = new ContentValues();
        cv.put("nom", e.getNom());
        cv.put("nom_arabe", e.getNomArab());
        cv.put("type", e.getType());
        cv.put("ville", e.getVille());
        cv.put("prix", e.getPrix());
        cv.put("duree", e.getDuree());
        cv.put("diplome", e.getDiplome());
        cv.put("filiere", e.getFiliere());
        return getWritableDatabase()
                .update("ecoles", cv, "id=?",
                        new String[]{String.valueOf(e.getId())}) > 0;
    }

    public boolean deleteEcole(long id) {
        return getWritableDatabase()
                .delete("ecoles", "id=?", new String[]{String.valueOf(id)}) > 0;
    }



    public long addUserEcole(long userId, long ecoleId) {
        ContentValues cv = new ContentValues();
        cv.put("user_id", userId);
        cv.put("ecole_id", ecoleId);
        return getWritableDatabase().insert(TABLE_USER_ECOLES, null, cv);
    }

    public boolean isUserEcoleExists(long userId, long ecoleId) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM user_ecoles WHERE user_id=? AND ecole_id=?",
                new String[]{String.valueOf(userId), String.valueOf(ecoleId)});
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count > 0;
    }

    public List<Ecole> getUserEcoles(long userId) {
        List<Ecole> list = new ArrayList<>();
        try {
            Cursor c = getReadableDatabase().rawQuery(
                    "SELECT e.* FROM ecoles e " +
                            "INNER JOIN user_ecoles ue ON e.id = ue.ecole_id " +
                            "WHERE ue.user_id = ?",
                    new String[]{String.valueOf(userId)});

            while (c.moveToNext()) {
                Ecole e = new Ecole(
                        c.getString(1), c.getString(2), c.getString(3),
                        c.getString(4), c.getString(5), c.getString(6),
                        c.getString(7), c.getString(8));
                e.setId(c.getLong(0));
                list.add(e);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getUserEcoleCount(long userId) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM user_ecoles WHERE user_id=?",
                new String[]{String.valueOf(userId)});
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public boolean deleteUserEcole(long userId, long ecoleId) {
        return getWritableDatabase().delete("user_ecoles",
                "user_id=? AND ecole_id=?",
                new String[]{String.valueOf(userId), String.valueOf(ecoleId)}) > 0;
    }
}