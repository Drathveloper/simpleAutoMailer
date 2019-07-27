package org.drathveloper;

import java.util.*;

public class ArgsParser {

    private Map<String, List<String>> arguments;

    public ArgsParser(String[] arguments){
        this.arguments = this.processArguments(arguments);
    }

    public ArgsParser(String[] arguments, String[] requiredOptions) throws IllegalArgumentException {
        this(arguments);
        if(!this.areAllOptionsInList(requiredOptions)){
            throw new IllegalArgumentException("Not enough parameters");
        }
    }

    public ArgsParser(String[] arguments, String[] requiredOptions, String[] optionalOptions){
        this(arguments);
        if(!this.areAllOptionsInList(requiredOptions) || this.areAllLegalOptions(requiredOptions, optionalOptions)){
            throw new IllegalArgumentException("Not enough parameters");
        }
    }

    public Map<String, List<String>> processArguments(String[] arguments){
        Map<String, List<String>> params = new HashMap<>();
        String auxParam = null;
        List<String> auxList = new ArrayList<>();
        for(String param : arguments){
            if(param.charAt(0) == '-'){
                if(auxParam!=null){
                    params.put(auxParam, new ArrayList<>(auxList));
                    auxList = new ArrayList<>();
                }
                auxParam = param;
            } else {
                auxList.add(param);
            }
        }
        if(auxParam!=null){
            params.put(auxParam, new ArrayList<>(auxList));
        }
        return params;
    }

    public List<String> getParametersFromOption(String option){
        if(arguments.get(option)!=null){
            return arguments.get(option);
        }
        return new ArrayList<>();
    }

    private boolean areAllLegalOptions(String[] requiredOptions, String[] optionalOptions){
        return isValidOptionList(requiredOptions) && isValidOptionList(optionalOptions);
    }

    private boolean areAllOptionsInList(String[] expectedOptions){
        int foundOptions = 0;
        for(String option : expectedOptions){
            if(this.isOptionInList(option)){
                foundOptions++;
            }
        }
        return foundOptions == expectedOptions.length;
    }

    private boolean isValidOptionList(String[] optionList){
        for(String option : optionList){
            if(!isOptionInList(option)){
                return false;
            }
        }
        return true;
    }

    private boolean isOptionInList(String option) {
        return arguments.keySet().contains(option);
    }

}
