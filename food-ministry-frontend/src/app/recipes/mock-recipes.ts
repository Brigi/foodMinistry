import { Ingredient } from '../ingredient/ingredient';
import { Recipe } from './recipe';

const TOMATO_SALAD_DESCRIPTION = 'Everybody looooves tomato salad. This recipe is inspired by my grandma as all the recipes in the world.' +
 ' Chop the tomatoes and onions in slices. Put them in a bowl and add the salt, pepper, olive oil and vinegar to it.' +
 ' Serve this meal as long as it is still tasty.';

const LOREM_IPSUM = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum vitae rutrum metus, at mattis arcu. ' +
'Phasellus tempor eu velit at gravida. Aenean ac dapibus velit. Sed vitae nunc ligula. Vivamus non purus id sem malesuada hendrerit ac non risus. ' +
'Pellentesque malesuada metus vel velit maximus, quis auctor est consectetur. Curabitur varius pretium tincidunt. In vulputate tortor elit, ' +
'et molestie ligula tempus ut. Quisque venenatis pretium venenatis.\n' +
'Fusce bibendum libero sed ornare lobortis. Nulla tincidunt augue sapien, eu malesuada metus congue tempus. ' +
'Nunc rutrum sem vulputate nisl auctor aliquam. Phasellus quam lacus, facilisis sed enim eget, varius gravida nunc. ' +
'Donec tempus pretium justo ut facilisis. Nulla vulputate rhoncus augue, in convallis urna tincidunt at. Phasellus eu euismod dui. ' +
'Nullam felis erat, egestas nec neque ac, viverra condimentum elit.\n' +
'Aenean accumsan maximus tempor. Maecenas pretium fringilla libero et cursus. Proin et convallis nibh, vitae suscipit neque. ' +
'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Lorem ipsum dolor sit amet, consectetur adipiscing elit. ' +
'Nam a quam purus. Mauris neque sapien, porta in felis non, varius posuere enim. Morbi a nunc scelerisque, feugiat odio at, ullamcorper est. ' +
'Vestibulum posuere mauris sit amet diam rhoncus cursus.\n' +
'In condimentum gravida commodo. Nulla dignissim suscipit maximus. Morbi tincidunt sollicitudin neque eget interdum. ' +
'Vivamus a massa accumsan, porta tortor eget, egestas diam. In hac habitasse platea dictumst. Nulla facilisi. Donec ut dictum sapien. ' +
'Pellentesque velit turpis, placerat vitae nunc id, porta sodales metus.\n' +
'Suspendisse potenti. Sed dapibus lacus velit, eu varius augue tempus ac. Etiam tempor malesuada elit ac maximus. ' +
'Quisque quis malesuada est, blandit auctor nulla. Etiam finibus tempor consequat. Ut vitae mollis ex. Maecenas semper ex sit amet leo accumsan convallis. ' +
'Suspendisse volutpat quis risus ac elementum. Morbi scelerisque eu lorem ut consequat.';

export const RECIPES: Map<number, Recipe> = new Map([
  [1, new Recipe(1, 'Tomato Salad', TOMATO_SALAD_DESCRIPTION, [
    { id: 3, name: 'Tomato', unit: 'none', isBasic: false },
    { id: 13, name: 'Onions', unit: 'none', isBasic: false },
    { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true },
    { id: 14, name: 'Pepper', unit: 'tsp.', isBasic: true },
    { id: 9, name: 'Olive Oil', unit: 'ml', isBasic: true },
    { id: 12, name: 'Vinegar', unit: 'ml', isBasic: true }
  ])],
  [2, new Recipe(2, 'Tasty Lorem', LOREM_IPSUM, [
    { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }
  ])],
  [3, new Recipe(3, 'Tasty Lorem', LOREM_IPSUM, [
    { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }
  ])],
  [4, new Recipe(4, 'Tasty Lorem', LOREM_IPSUM, [
    { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }
  ])],
  [5, new Recipe(5, 'Tasty Lorem', LOREM_IPSUM, [
    { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }
  ])],
  [6, new Recipe(6, 'Tasty Lorem', LOREM_IPSUM, [
    { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }
  ])],
  [7, new Recipe(7, 'Tasty Lorem', LOREM_IPSUM, [
    { id: 2, name: 'Salt', unit: 'tsp.', isBasic: true }
  ])]
]);
