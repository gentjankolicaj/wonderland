package io.wonderland.rh.monitor;

import io.wonderland.rh.base.fx.base.NodeDynamicMBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.Parent;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;


@Slf4j
public final class JMXBase {

  public static final int REFRESH_DELAY = 1000;
  private static final Set<Node> NODES = Collections.synchronizedSet(new HashSet<>());
  private static MBeanServer mBeanServer;

  private JMXBase() {
  }


  public static void start() {
    if (mBeanServer == null) {
      mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }
    try {
      //management beans
      for (Node node : NODES) {
        NodeDynamicMBean NodeDynamicMBean = new NodeDynamicMBean(node);
        mBeanServer.registerMBean(NodeDynamicMBean, NodeDynamicMBean.getObjectName());
      }
    } catch (Exception e) {
      log.error("", e);
    }
  }


  private static void updateMBeans() throws JMException, IllegalAccessException {
    //This retrieves all registered MBeans.
    // The null parameters mean that no specific query is applied, so it returns all MBeans.
    if (mBeanServer != null) {
      Set<ObjectInstance> allBeans = mBeanServer.queryMBeans(null, null);
      if (CollectionUtils.isNotEmpty(allBeans)) {
        //create new mbeans
        List<NodeDynamicMBean> fxNodes = NODES.stream().map(NodeDynamicMBean::create)
            .collect(Collectors.toList());

        // remove all my domain beans , only my beans not all because there are management beans as well
        List<ObjectInstance> myBeans = allBeans.stream()
            .filter(e -> e.getObjectName().getDomain().equals(NodeDynamicMBean.DOMAIN)).collect(
                Collectors.toList());

        //debugging
        //log.info("All MBeans {}, rabbit-hole MBeans {}",allBeans.size(),myBeans.size());

        List<ObjectInstance> deadBeans = new ArrayList<>();
        List<NodeDynamicMBean> aliveBeans = new ArrayList<>();
        for (ObjectInstance oi : myBeans) {
          boolean found = false;
          for (NodeDynamicMBean fxNode : fxNodes) {
            if (fxNode.getObjectName().compareTo(oi.getObjectName()) == 0) {
              found = true;
              aliveBeans.add(fxNode);
              break;
            }
          }
          if (!found) {
            deadBeans.add(oi);
          }
        }

        //remove my dead beans
        deadBeans.forEach(bean -> {
          try {
            mBeanServer.unregisterMBean(bean.getObjectName());
          } catch (JMException e) {
            throw new RuntimeException(e);
          }
        });

        //remove duplicate beans
        fxNodes.removeAll(aliveBeans);

        //add new beans
        fxNodes.forEach(node -> {
          try {
            mBeanServer.registerMBean(node, node.getObjectName());
          } catch (JMException e) {
            throw new RuntimeException(e);
          }
        });

      } else {
        for (Node node : NODES) {
          NodeDynamicMBean fxNode = new NodeDynamicMBean(node);
          mBeanServer.registerMBean(fxNode, fxNode.getObjectName());
        }
      }
    }
  }


  public static void addParentNode(Node node) {
    if (Objects.isNull(node)) {
      throw new IllegalArgumentException("Can't register empty node");
    }
    NODES.add(node);
    startMonitorThread(node);
  }

  // Add a listener to the children of the VBox
  private static void startMonitorThread(Node node) {
    CompletableFuture.runAsync(() -> {
      while (true) {
        try {
          updateNodes(node);
          updateMBeans();
          Thread.sleep(REFRESH_DELAY);
        } catch (Exception e) {
          log.error("", e);
          break;
        }
      }
    });

  }

  private static void updateNodes(Node root) {
    //clear set
    NODES.clear();
    //re-populate set
    findDescendants(root, NODES);
  }

  private static void findDescendants(Node parent, Set<Node> nodes) {
    nodes.add(parent);
    if (parent instanceof Parent) {
      Parent pane = (Parent) parent;
      for (Node node : pane.getChildrenUnmodifiable()) {
        findDescendants(node, nodes);
      }
    }
  }

}
