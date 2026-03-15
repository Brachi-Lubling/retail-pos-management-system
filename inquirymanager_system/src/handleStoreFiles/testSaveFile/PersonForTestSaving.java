package handleStoreFiles.testSaveFile;

import handleStoreFiles.ForSaving;

public class PersonForTestSaving implements ForSaving
{
    String id;
    String name;

    public PersonForTestSaving(String id,String name)
    {
        this.id=id;
        this.name=name;
    }

    @Override
    public String getFolderName(){
        return getClass().getPackageName();
    }

    @Override
    public String getFileName(){
        return getClass().getSimpleName();
    }

    @Override
    public String getData() {
        return id+ "," + name;
    }

}
