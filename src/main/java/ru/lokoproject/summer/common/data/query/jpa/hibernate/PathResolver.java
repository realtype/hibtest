package ru.lokoproject.summer.common.data.query.jpa.hibernate;

import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

import static ru.lokoproject.summer.common.data.util.FieldPathUtil.*;

@SuppressWarnings("rawtypes")
public class PathResolver {

    public From resolvePath(String path, Root root, Map<String, From> joinMap, boolean ignoreLastPath){
        return resolvePath(path, root, joinMap, ignoreLastPath, null);
    }

    public From resolvePath(String path, Root root, Map<String, From> joinMap, boolean ignoreLastPath, Predicate joinPredicate){
        if(isFieldPathFinal(path)){
            return root;
        }
        else {
            From result = tryToFindJoinAndExtendItRecur(path, joinMap, ignoreLastPath, joinPredicate);
            if(result != null){
                joinMap.put(path, result);
                return result;
            }

            result = createJoin(path, root, ignoreLastPath, joinPredicate);
            if(result != null){
                joinMap.put(path, result);
                return result;
            }

            return null;
        }
    }

    private From createJoin(String path, From root, boolean ignoreLastPath, Predicate joinPredicate) {
        String[] partsOfRightPath = splitPath(path);
        int lengthToIterate = ignoreLastPath ? partsOfRightPath.length -1 : partsOfRightPath.length;
        for(int i=0; i < lengthToIterate; i++){
            root = root.join(partsOfRightPath[i], JoinType.INNER);
        }
        return root;
    }

    private From tryToFindJoinAndExtendItRecur(String path, Map<String, From> joinMap, boolean ignoreLastPath, Predicate joinPredicate) {
        String leftPath = dropLastPartOfPath(path);
        From from = joinMap.get(leftPath);
        if(from == null){
            if(isFieldPathFinal(leftPath)){
                return null;
            }
            else{
                return tryToFindJoinAndExtendItRecur(leftPath, joinMap, ignoreLastPath, joinPredicate);
            }
        }
        else{
            String rightPath = getLastPartOfPath(path);
            return createJoin(rightPath, from, ignoreLastPath, joinPredicate);
        }
    }
}
