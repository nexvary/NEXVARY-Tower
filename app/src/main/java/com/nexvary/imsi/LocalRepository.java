package com.nexvary.imsi;

import android.content.Context;
import org.json.*;
import java.util.*;

public final class LocalRepository {
    private final SecureStore store;
    public LocalRepository(Context c){ store=new SecureStore(c); }
    public List<ScanRecord> history(){ List<ScanRecord> out=new ArrayList<>(); try{ JSONArray a=new JSONArray(store.get("history","[]")); for(int i=0;i<a.length();i++) out.add(ScanRecord.from(a.getJSONObject(i))); }catch(Exception ignored){} return out; }
    public List<String> baselines(){ List<String> out=new ArrayList<>(); try{ JSONArray a=new JSONArray(store.get("baselines","[]")); for(int i=0;i<a.length();i++) out.add(a.optString(i)); }catch(Exception ignored){} return out; }
    public void saveBaselines(List<String> items){ JSONArray a=new JSONArray(); for(String s:items)a.put(s); store.put("baselines",a.toString()); }
    public void clearHistory(){ store.remove("history"); }
}
