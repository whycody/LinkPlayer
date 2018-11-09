    public boolean getBooleanValue(String value){
        return sharedPreferences.getBoolean(value, false);
    }
